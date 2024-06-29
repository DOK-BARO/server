package kr.kro.dokbaro.server.domain.account.adapter.output.jooq

import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kr.kro.dokbaro.server.configuration.TestcontainersConfiguration
import kr.kro.dokbaro.server.domain.account.model.Account
import kr.kro.dokbaro.server.domain.account.model.Provider
import org.jooq.Configuration
import org.jooq.generated.tables.daos.AccountDao
import org.jooq.generated.tables.daos.RoleDao
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.transaction.annotation.Transactional
import java.time.Clock

@SpringBootTest
@Import(TestcontainersConfiguration::class)
@Transactional
class AccountCommandRepositoryTest(
	private val accountCommandRepository: AccountCommandRepository,
	private val configuration: Configuration,
) : StringSpec({
		extensions(SpringTestExtension(SpringTestLifecycleMode.Root))

		val clock = Clock.systemUTC()
		val accountDao = AccountDao(configuration)
		val roleDao = RoleDao(configuration)

		"저장을 수행한다" {

			val account =
				Account.init(
					"abcdefg",
					Provider.KAKAO,
					clock,
				)
			val savedAccountId = accountCommandRepository.save(account)

			val accountResult = accountDao.findById(savedAccountId)
			val roles = roleDao.findAll().filter { it.accountId.equals(savedAccountId) }

			accountResult shouldNotBe null
			roles.size shouldBe account.roles.size
		}

		"존재 여부를 판단한다" {
			val socialId = "abcdefg"
			accountCommandRepository.save(
				Account.init(
					socialId,
					Provider.KAKAO,
					clock,
				),
			)

			accountCommandRepository.existBy(socialId) shouldBe true
			accountCommandRepository.notExistBy(socialId) shouldBe false
			accountCommandRepository.existBy("qwersdaf") shouldBe false
			accountCommandRepository.notExistBy("qwersdaf") shouldBe true
		}
	})