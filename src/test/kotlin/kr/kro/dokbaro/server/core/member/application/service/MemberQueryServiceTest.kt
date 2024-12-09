package kr.kro.dokbaro.server.core.member.application.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import kr.kro.dokbaro.server.core.member.application.port.out.LoadMemberByCertificationIdPort
import kr.kro.dokbaro.server.core.member.application.port.out.ReadCertificatedMemberPort
import kr.kro.dokbaro.server.core.member.application.port.out.ReadCertificationIdByEmailPort
import kr.kro.dokbaro.server.core.member.application.port.out.ReadEmailAuthenticationMemberPort
import kr.kro.dokbaro.server.core.member.application.service.exception.NotFoundCertificationMemberException
import kr.kro.dokbaro.server.core.member.application.service.exception.NotFoundMemberException
import kr.kro.dokbaro.server.core.member.query.CertificatedMember
import kr.kro.dokbaro.server.fixture.domain.memberFixture
import java.util.UUID

class MemberQueryServiceTest :
	StringSpec({
		val readEmailAuthenticationMemberPort = mockk<ReadEmailAuthenticationMemberPort>()
		val readCertificatedMemberPort = mockk<ReadCertificatedMemberPort>()
		val readCertificationIdByEmailPort = mockk<ReadCertificationIdByEmailPort>()
		val loadMemberByCertificationIdPort = mockk<LoadMemberByCertificationIdPort>()

		val memberQueryService =
			MemberQueryService(
				readEmailAuthenticationMemberPort,
				readCertificatedMemberPort,
				readCertificationIdByEmailPort,
				loadMemberByCertificationIdPort,
			)

		"EmailAuthenticationMember 를 조회한다" {
			every { readEmailAuthenticationMemberPort.findEmailAuthenticationMember(any()) } returns null

			memberQueryService.findEmailAuthenticationMember("aaa@bb.com") shouldBe null
		}

		"CertificatedMember를 조회한다" {
			every { readCertificatedMemberPort.findCertificatedMember(any()) } returns null

			shouldThrow<NotFoundCertificationMemberException> {
				memberQueryService.findCertificationMember(UUID.randomUUID())
			}

			every { readCertificatedMemberPort.findCertificatedMember(any()) } returns
				CertificatedMember(
					id = 1L,
					certificationId = UUID.randomUUID(),
					nickname = "KotlinFan",
					email = "kotlinfan@example.com",
					role = listOf("USER", "ADMIN"),
				)

			memberQueryService.findCertificationMember(UUID.randomUUID()) shouldNotBe null
		}

		"email에 해당하는 certificationId를 조회한다" {
			every { readCertificationIdByEmailPort.findCertificationIdByEmail(any()) } returns
				UUID.randomUUID()

			memberQueryService.findCertificationIdByEmail("aaa@bb.com") shouldNotBe null
		}

		"내 avatar를 조회한다" {
			every { loadMemberByCertificationIdPort.findMemberByCertificationId(any()) } returns null

			shouldThrow<NotFoundMemberException> {
				memberQueryService.findMyAvatar(UUID.randomUUID())
			}

			every { loadMemberByCertificationIdPort.findMemberByCertificationId(any()) } returns memberFixture()

			memberQueryService.findMyAvatar(UUID.randomUUID()) shouldNotBe null
		}
	})