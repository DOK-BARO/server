package kr.kro.dokbaro.server.domain.account.adapter.output.jooq

import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import io.kotest.matchers.shouldBe
import kr.kro.dokbaro.server.TestcontainersConfiguration
import kr.kro.dokbaro.server.domain.account.model.Account
import kr.kro.dokbaro.server.domain.account.model.Provider
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.transaction.annotation.Transactional
import java.time.Clock

@SpringBootTest
@Import(TestcontainersConfiguration::class)
@Transactional
class AccountQueryRepositoryTest(
	private val accountCommandRepository: AccountCommandRepository,
	private val accountQueryRepository: AccountQueryRepository,
) : StringSpec({
		extensions(SpringTestExtension(SpringTestLifecycleMode.Root))

		val clock = Clock.systemUTC()
		val socialId = "adsfqwer"

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