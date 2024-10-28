package kr.kro.dokbaro.server.core.termsofservice.application.service

import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import kr.kro.dokbaro.server.core.member.application.port.input.query.FindCertificatedMemberUseCase
import kr.kro.dokbaro.server.core.termsofservice.application.port.out.InsertAgreeTermsOfServicePersistencePort
import kr.kro.dokbaro.server.core.termsofservice.application.port.out.LoadTermsOfServiceDetailPort
import kr.kro.dokbaro.server.core.termsofservice.query.TermsOfServiceDetail
import kr.kro.dokbaro.server.fixture.domain.certificatedMemberFixture
import java.util.UUID

class TermsOfServiceServiceTest :
	StringSpec({
		val loadTermsOfServiceDetailPort = mockk<LoadTermsOfServiceDetailPort>()
		val insertAgreeTermsOfServicePersistencePort = mockk<InsertAgreeTermsOfServicePersistencePort>()
		val certificatedMemberUseCase = mockk<FindCertificatedMemberUseCase>()

		val termsOfServiceService =
			TermsOfServiceService(
				loadTermsOfServiceDetailPort,
				insertAgreeTermsOfServicePersistencePort,
				certificatedMemberUseCase,
			)

		"서비스 이용 약관 목록을 조회한다" {
			termsOfServiceService.findAll().shouldNotBeEmpty()
		}

		"서비스 이용 약관 상세 내용을 조회한다" {
			every { loadTermsOfServiceDetailPort.getDetail(any()) } returns TermsOfServiceDetail("상세내용")

			termsOfServiceService.findDetail(3) shouldNotBe null
		}

		"서비스 이용 약관 조회 시 상세 내용이 없으면 예외를 반환한다" {
			every { loadTermsOfServiceDetailPort.getDetail(any()) } returns null

			shouldThrow<NotFoundTermsOfServiceException> {
				termsOfServiceService.findDetail(123)
			}
		}

		"서비스 이용 약관에 동의한다" {
			every { certificatedMemberUseCase.getByCertificationId(any()) } returns certificatedMemberFixture()
			every { insertAgreeTermsOfServicePersistencePort.insertAgree(any()) } returns Unit

			shouldNotThrow<NotFoundTermsOfServiceException> {
				termsOfServiceService.agree(UUID.randomUUID(), listOf(1, 2, 3))
			}
		}

		"서비스 이용 약관 동의 시 관리하지 않는 ID에 동의하면 예외를 반환한다" {
			every { certificatedMemberUseCase.getByCertificationId(any()) } returns certificatedMemberFixture()

			shouldThrow<NotFoundTermsOfServiceException> {
				termsOfServiceService.agree(UUID.randomUUID(), listOf(1, 2, 3, 7))
			}
		}
	})