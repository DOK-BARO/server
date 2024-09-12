package kr.kro.dokbaro.server.core.auth.oauth2.adapter.out.persistence

import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kr.kro.dokbaro.server.common.type.AuthProvider
import kr.kro.dokbaro.server.configuration.annotation.PersistenceAdapterTest
import kr.kro.dokbaro.server.core.auth.oauth2.adapter.out.persistence.entity.jooq.OAuth2AccountMapper
import kr.kro.dokbaro.server.core.auth.oauth2.adapter.out.persistence.repository.jooq.OAuth2AccountQueryRepository
import kr.kro.dokbaro.server.core.auth.oauth2.adapter.out.persistence.repository.jooq.OAuth2AccountRepository
import kr.kro.dokbaro.server.core.auth.oauth2.domain.OAuth2Account
import kr.kro.dokbaro.server.core.auth.oauth2.domain.OAuth2CertificatedAccount
import kr.kro.dokbaro.server.core.member.adapter.out.persistence.entity.jooq.MemberMapper
import kr.kro.dokbaro.server.core.member.adapter.out.persistence.repository.jooq.MemberRepository
import kr.kro.dokbaro.server.fixture.domain.memberFixture
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
		val memberRepository = MemberRepository(dslContext, MemberMapper())

		val adapter = OAuth2AccountPersistenceAdapter(accountRepository, accountQueryRepository)

		"저장을 수행한다" {
			val member = memberRepository.insert(memberFixture())
			val account = OAuth2Account("aaaaa", AuthProvider.KAKAO, member.id)
			adapter.insert(account) shouldNotBe null
		}

		"socialId와 provider를 통한 조회를 수행한다" {
			val targetSocialId = "aaaaaa"
			val targetProvider = AuthProvider.KAKAO

			val member = memberRepository.insert(memberFixture())
			adapter.insert(OAuth2Account(targetSocialId, targetProvider, member.id))

			val result: OAuth2CertificatedAccount = adapter.findBy(targetSocialId, targetProvider)!!

			result.certificationId shouldNotBe null
			result.role.shouldNotBeEmpty()
		}

		"존재 여부를 판단한다" {
			val targetSocialId = "aaa"
			val targetProvider = AuthProvider.KAKAO

			val member = memberRepository.insert(memberFixture())
			adapter.insert(OAuth2Account(targetSocialId, targetProvider, member.id))

			adapter.existBy(targetSocialId, targetProvider) shouldBe true
			adapter.existBy("bbbb", targetProvider) shouldBe false
			adapter.existBy(targetSocialId, AuthProvider.GOOGLE) shouldBe false

			adapter.notExistBy(targetSocialId, targetProvider) shouldBe false
			adapter.notExistBy("bbbb", targetProvider) shouldBe true
			adapter.notExistBy(targetSocialId, AuthProvider.GOOGLE) shouldBe true
		}
	})