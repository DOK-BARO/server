package kr.kro.dokbaro.server.core.termsofservice.application.service

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kr.kro.dokbaro.server.core.member.application.port.input.query.FindCertificatedMemberUseCase
import kr.kro.dokbaro.server.core.termsofservice.application.port.out.ReadMemberAgreeTermsOfServicePort
import kr.kro.dokbaro.server.core.termsofservice.application.port.out.dto.MemberAgreeTermsOfServiceElement
import kr.kro.dokbaro.server.fixture.domain.certificatedMemberFixture
import java.util.UUID

class TermsOfServiceQueryServiceTest :
	StringSpec({
		val findCertificatedMemberUseCase = mockk<FindCertificatedMemberUseCase>()
		val readMemberAgreeTermsOfServicePort = mockk<ReadMemberAgreeTermsOfServicePort>()

		val termsOfServiceQueryService =
			TermsOfServiceQueryService(findCertificatedMemberUseCase, readMemberAgreeTermsOfServicePort)

		"member가 필수 목록에 전체 동의 시 true를 반환한다" {
			every { findCertificatedMemberUseCase.getByCertificationId(any()) } returns certificatedMemberFixture()
			every { readMemberAgreeTermsOfServicePort.findAll(any()) } returns
				listOf(
					MemberAgreeTermsOfServiceElement(1),
					MemberAgreeTermsOfServiceElement(2),
				)

			termsOfServiceQueryService.findBy(UUID.randomUUID()).agreeAll shouldBe true
		}

		"member가 필수 목록에 전체 동의를 안했을 시 false 를 반환한다" {
			every { findCertificatedMemberUseCase.getByCertificationId(any()) } returns certificatedMemberFixture()
			every { readMemberAgreeTermsOfServicePort.findAll(any()) } returns
				listOf(
					MemberAgreeTermsOfServiceElement(1),
				)

			termsOfServiceQueryService.findBy(UUID.randomUUID()).agreeAll shouldBe false
		}
	})