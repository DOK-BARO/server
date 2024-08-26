package kr.kro.dokbaro.server.core.auth.oauth2.adapter.out.persistence

import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import io.kotest.matchers.shouldNotBe
import kr.kro.dokbaro.server.common.type.AuthProvider
import kr.kro.dokbaro.server.configuration.annotation.PersistenceAdapterTest
import kr.kro.dokbaro.server.core.auth.oauth2.adapter.out.persistence.entity.jooq.OAuth2AccountMapper
import kr.kro.dokbaro.server.core.auth.oauth2.adapter.out.persistence.repository.jooq.OAuth2AccountQueryRepository
import kr.kro.dokbaro.server.core.auth.oauth2.adapter.out.persistence.repository.jooq.OAuth2AccountRepository
import kr.kro.dokbaro.server.core.auth.oauth2.domain.OAuth2Account
import org.jooq.Configuration
import org.jooq.DSLContext

@PersistenceAdapterTest
class OAuth2AccountPersistenceAdapterTest(
	private val dslContext: DSLContext,
	private val configuration: Configuration,
) : StringSpec({

		extensions(SpringTestExtension(SpringTestLifecycleMode.Root))

		val accountRepository = OAuth2AccountRepository(dslContext)
		val accountQueryRepository = OAuth2AccountQueryRepository(dslContext, OAuth2AccountMapper())
		val adapter = OAuth2AccountPersistenceAdapter(accountRepository, accountQueryRepository)

		afterEach {
			dslContext.rollback()
		}

		"저장을 수행한다" {
			TODO("member 저장해")
			val account = OAuth2Account("aaa", AuthProvider.KAKAO, 5)
			adapter.save(account) shouldNotBe null
		}

		"socialId와 provider를 통한 조회를 수행한다" {
			val targetId = "aaa"
			val targetProvider = AuthProvider.KAKAO

			TODO("member 저장해")
			adapter.save(OAuth2Account(targetId, targetProvider, 5))
		}

		"존재 여부를 판단한다" {
		}
	})