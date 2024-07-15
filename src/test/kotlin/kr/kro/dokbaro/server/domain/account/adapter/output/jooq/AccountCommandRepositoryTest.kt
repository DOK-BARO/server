package kr.kro.dokbaro.server.domain.account.adapter.output.jooq

import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kr.kro.dokbaro.server.configuration.TestcontainersConfiguration
import kr.kro.dokbaro.server.domain.account.model.Account
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

		val accountCommandRepository = AccountCommandRepository(dslContext)

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
					AuthProvider.KAKAO,
					clock,
				),
			)

			accountCommandRepository.existBy(socialId, AuthProvider.KAKAO) shouldBe true
			accountCommandRepository.notExistBy(socialId, AuthProvider.KAKAO) shouldBe false
			accountCommandRepository.existBy(socialId, AuthProvider.GOOGLE) shouldBe false
			accountCommandRepository.existBy("qwersdaf", AuthProvider.KAKAO) shouldBe false
			accountCommandRepository.notExistBy("qwersdaf", AuthProvider.KAKAO) shouldBe true
		}
	})