package kr.kro.dokbaro.server.core.member.adapter.out

import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kr.kro.dokbaro.server.configuration.annotation.PersistenceAdapterTest
import kr.kro.dokbaro.server.core.account.adapter.out.persistence.repository.jooq.AccountRepository
import kr.kro.dokbaro.server.core.account.domain.AuthProvider
import kr.kro.dokbaro.server.core.account.domain.SocialAccount
import kr.kro.dokbaro.server.core.member.adapter.out.persistence.entity.jooq.MemberMapper
import kr.kro.dokbaro.server.core.member.adapter.out.persistence.repository.jooq.MemberQueryRepository
import kr.kro.dokbaro.server.core.member.adapter.out.persistence.repository.jooq.MemberRepository
import kr.kro.dokbaro.server.core.member.domain.Email
import kr.kro.dokbaro.server.fixture.domain.memberFixture
import org.jooq.DSLContext
import java.util.UUID

@PersistenceAdapterTest
class MemberPersistenceQueryAdapterTest(
	private val dslContext: DSLContext,
) : StringSpec({
		extensions(SpringTestExtension(SpringTestLifecycleMode.Root))

		val memberRepository = MemberRepository(dslContext, MemberMapper())
		val memberQueryRepository = MemberQueryRepository(dslContext, MemberMapper())
		val accountRepository = AccountRepository(dslContext)

		val queryAdapter = MemberPersistenceQueryAdapter(memberQueryRepository)

		"email을 통한 EmailAuthenticationMember 조회를 수행한다" {
			val email = Email("hello@example.com")
			memberRepository.insert(memberFixture(email = email))

			val result = queryAdapter.findEmailAuthenticationMember(email.address)

			result!!.email shouldBe email.address
		}

		"certificationId를 통한 CertificatedMember 조회를 수행한다" {
			val certificationId = UUID.randomUUID()
			memberRepository.insert(memberFixture(certificationId = certificationId))

			queryAdapter.findCertificatedMember(certificationId)!!.certificationId shouldBe certificationId
		}

		"certificationId를 통한 member 조회를 수행한다" {
			val certificationId = UUID.randomUUID()
			memberRepository.insert(memberFixture(certificationId = certificationId))

			queryAdapter.findMemberByCertificationId(certificationId)!!.certificationId shouldBe certificationId
		}

		"email을 통한 certificationId를 조회한다" {
			val email = Email("hello@example.com")
			memberRepository.insert(memberFixture(email = email))

			queryAdapter.findCertificationIdByEmail(email.address) shouldNotBe null
			queryAdapter.findCertificationIdByEmail("no@no.com") shouldBe null
		}

		"social 계정을 통한 certificationId를 조회한다" {
			val uuid = UUID.randomUUID()
			val member = memberRepository.insert(memberFixture(certificationId = uuid))
			val socialId = "socialId"
			val provider = AuthProvider.KAKAO

			accountRepository.insertSocialAccount(
				SocialAccount(
					socialId = socialId,
					provider = provider,
					memberId = member.id,
				),
			)

			queryAdapter.findCertificationIdBySocial(socialId, provider) shouldBe uuid
		}
	})