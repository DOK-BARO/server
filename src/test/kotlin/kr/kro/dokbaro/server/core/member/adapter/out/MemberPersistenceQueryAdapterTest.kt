package kr.kro.dokbaro.server.core.member.adapter.out

import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kr.kro.dokbaro.server.configuration.annotation.PersistenceAdapterTest
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

		"email 존재 여부를 확인한다" {
			val email = Email("hello@example.com")
			memberRepository.insert(memberFixture(email = email))

			queryAdapter.existByEmail(email.address) shouldBe true
			queryAdapter.existByEmail("false@gmail.com") shouldBe false
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
	})