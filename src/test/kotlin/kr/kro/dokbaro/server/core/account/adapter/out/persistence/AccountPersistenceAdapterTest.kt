package kr.kro.dokbaro.server.core.account.adapter.out.persistence

import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kr.kro.dokbaro.server.common.type.AuthProvider
import kr.kro.dokbaro.server.configuration.annotation.PersistenceAdapterTest
import kr.kro.dokbaro.server.core.account.adapter.out.persistence.entity.jooq.AccountMapper
import kr.kro.dokbaro.server.core.account.adapter.out.persistence.repository.jooq.AccountQueryRepository
import kr.kro.dokbaro.server.core.account.adapter.out.persistence.repository.jooq.AccountRepository
import kr.kro.dokbaro.server.core.account.domain.Account
import org.jooq.Configuration
import org.jooq.DSLContext
import org.jooq.generated.tables.daos.AccountDao
import org.jooq.generated.tables.daos.RoleDao
import java.time.Clock

@PersistenceAdapterTest
class AccountPersistenceAdapterTest(
	private val dslContext: DSLContext,
	private val configuration: Configuration,
) : StringSpec({

		extensions(SpringTestExtension(SpringTestLifecycleMode.Root))

		val accountRepository = AccountRepository(dslContext)
		val accountQueryRepository = AccountQueryRepository(dslContext, AccountMapper())
		val adapter = AccountPersistenceAdapter(accountRepository, accountQueryRepository)

		val clock = Clock.systemUTC()
		val accountDao = AccountDao(configuration)
		val roleDao = RoleDao(configuration)
		val socialId = "adsfqwer"

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
			val savedAccountId = adapter.save(account)

			val accountResult = accountDao.findById(savedAccountId)
			val roles = roleDao.findAll().filter { it.accountId.equals(savedAccountId) }

			accountResult shouldNotBe null
			roles.size shouldBe account.roles.size
		}

		"조회를 수행한다" {
			val account =
				Account.init(
					socialId,
					AuthProvider.GOOGLE,
					clock,
				)
			val savedId = adapter.save(account)

			val findResult: Account = adapter.findBy(socialId) ?: throw RuntimeException()

			findResult.id shouldBe savedId
			findResult.provider shouldBe account.provider
			findResult.socialId shouldBe account.socialId
			findResult.roles shouldBe account.roles
		}

		"존재 여부를 판단한다" {
			val socialId = "abcdefg"
			adapter.save(
				Account.init(
					socialId,
					AuthProvider.KAKAO,
					clock,
				),
			)

			adapter.existBy(socialId, AuthProvider.KAKAO) shouldBe true
			adapter.notExistBy(socialId, AuthProvider.KAKAO) shouldBe false
			adapter.existBy(socialId, AuthProvider.GOOGLE) shouldBe false
			adapter.existBy("qwersdaf", AuthProvider.KAKAO) shouldBe false
			adapter.notExistBy("qwersdaf", AuthProvider.KAKAO) shouldBe true
		}
	})