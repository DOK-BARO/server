package kr.kro.dokbaro.server.core.account.adapter.out.jooq

import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kr.kro.dokbaro.server.configuration.TestcontainersConfiguration
import kr.kro.dokbaro.server.core.account.adapter.out.persistence.repository.jooq.AccountRepository
import kr.kro.dokbaro.server.core.account.domain.Account
import kr.kro.dokbaro.server.global.AuthProvider
import org.jooq.Configuration
import org.jooq.DSLContext
import org.jooq.generated.tables.daos.AccountDao
import org.jooq.generated.tables.daos.RoleDao
import org.springframework.boot.test.autoconfigure.jooq.JooqTest
import org.springframework.context.annotation.Import
import java.time.Clock

@JooqTest
@Import(TestcontainersConfiguration::class)
class AccountCommandRepositoryTest(
	private val dslContext: DSLContext,
	private val configuration: Configuration,
) : StringSpec({
		extensions(SpringTestExtension(SpringTestLifecycleMode.Root))

		val accountRepository = AccountRepository(dslContext)

		val clock = Clock.systemUTC()
		val accountDao = AccountDao(configuration)
		val roleDao = RoleDao(configuration)

		afterEach {
			dslContext.rollback()
		}

		"저장을 수행한다" {

			val account =
				Account.init(
					"abcdefg",
					AuthProvider.KAKAO,
					clock,
				)
			val savedAccountId = accountRepository.save(account)

			val accountResult = accountDao.findById(savedAccountId)
			val roles = roleDao.findAll().filter { it.accountId.equals(savedAccountId) }

			accountResult shouldNotBe null
			roles.size shouldBe account.roles.size
		}

		"존재 여부를 판단한다" {
			val socialId = "abcdefg"
			accountRepository.save(
				Account.init(
					socialId,
					AuthProvider.KAKAO,
					clock,
				),
			)

			accountRepository.existBy(socialId, AuthProvider.KAKAO) shouldBe true
			accountRepository.notExistBy(socialId, AuthProvider.KAKAO) shouldBe false
			accountRepository.existBy(socialId, AuthProvider.GOOGLE) shouldBe false
			accountRepository.existBy("qwersdaf", AuthProvider.KAKAO) shouldBe false
			accountRepository.notExistBy("qwersdaf", AuthProvider.KAKAO) shouldBe true
		}
	})