package kr.kro.dokbaro.server.domain.account.adapter.output.jooq

import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import io.kotest.matchers.shouldBe
import kr.kro.dokbaro.server.configuration.TestcontainersConfiguration
import kr.kro.dokbaro.server.domain.account.model.Account
import kr.kro.dokbaro.server.domain.account.model.Provider
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

		val accountCommandRepository = AccountCommandRepository(dslContext)
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
					Provider.GOOGLE,
					clock,
				)
			val savedId = accountCommandRepository.save(account)

			val findResult: Account = accountQueryRepository.findBy(socialId) ?: throw RuntimeException()

			findResult.id shouldBe savedId
			findResult.provider shouldBe account.provider
			findResult.socialId shouldBe account.socialId
			findResult.roles shouldBe account.roles
		}
	})