package kr.kro.dokbaro.server.core.account.adapter.out.jooq

import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import io.kotest.matchers.shouldBe
import kr.kro.dokbaro.server.configuration.TestcontainersConfiguration
import kr.kro.dokbaro.server.core.account.adapter.out.persistence.entity.jooq.AccountMapper
import kr.kro.dokbaro.server.core.account.adapter.out.persistence.repository.jooq.AccountQueryRepository
import kr.kro.dokbaro.server.core.account.adapter.out.persistence.repository.jooq.AccountRepository
import kr.kro.dokbaro.server.core.account.domain.Account
import kr.kro.dokbaro.server.global.AuthProvider
import org.jooq.DSLContext
import org.springframework.boot.test.autoconfigure.jooq.JooqTest
import org.springframework.context.annotation.Import
import java.time.Clock

@JooqTest
@Import(TestcontainersConfiguration::class)
class AccountQueryRepositoryTest(
	private val dslContext: DSLContext,
) : StringSpec({
		extensions(SpringTestExtension(SpringTestLifecycleMode.Root))

		val accountRepository = AccountRepository(dslContext)
		val accountQueryRepository = AccountQueryRepository(dslContext, AccountMapper())

		val clock = Clock.systemUTC()
		val socialId = "adsfqwer"

		afterEach {
			dslContext.rollback()
		}

		"조회를 수행한다" {
			val account =
				Account.init(
					socialId,
					AuthProvider.GOOGLE,
					clock,
				)
			val savedId = accountRepository.save(account)

			val findResult: Account = accountQueryRepository.findBy(socialId) ?: throw RuntimeException()

			findResult.id shouldBe savedId
			findResult.provider shouldBe account.provider
			findResult.socialId shouldBe account.socialId
			findResult.roles shouldBe account.roles
		}
	})