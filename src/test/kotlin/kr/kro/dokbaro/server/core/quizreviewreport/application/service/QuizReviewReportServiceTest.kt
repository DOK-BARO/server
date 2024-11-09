package kr.kro.dokbaro.server.core.quizreviewreport.application.service

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import kr.kro.dokbaro.server.core.member.application.port.input.query.FindCertificatedMemberUseCase
import kr.kro.dokbaro.server.core.quizreviewreport.application.port.input.dto.CreateQuizReviewReportCommand
import kr.kro.dokbaro.server.core.quizreviewreport.application.port.out.InsertQuizReviewReportPort
import kr.kro.dokbaro.server.fixture.domain.certificatedMemberFixture
import java.util.UUID

class QuizReviewReportServiceTest :
	StringSpec({
		val findCertificatedMemberUseCase = mockk<FindCertificatedMemberUseCase>()
		val insertQuizReviewReportPort = mockk<InsertQuizReviewReportPort>()

		val quizReviewReportService = QuizReviewReportService(findCertificatedMemberUseCase, insertQuizReviewReportPort)

		"quizReviewReport를 생성한다" {
			every { findCertificatedMemberUseCase.getByCertificationId(any()) } returns certificatedMemberFixture()
			every { insertQuizReviewReportPort.insert(any()) } returns 1

			quizReviewReportService.create(
				CreateQuizReviewReportCommand(
					UUID.randomUUID(),
					1,
					"hello",
				),
			) shouldNotBe null
		}
	})