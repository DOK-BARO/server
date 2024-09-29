package kr.kro.dokbaro.server.core.bookquiz.application.service

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kr.kro.dokbaro.server.core.bookquiz.application.port.input.dto.CreateBookQuizCommand
import kr.kro.dokbaro.server.core.bookquiz.application.port.out.InsertBookQuizPort
import kr.kro.dokbaro.server.core.member.application.port.input.query.FindCertificatedMemberUseCase
import kr.kro.dokbaro.server.fixture.domain.memberResponseFixture
import java.util.UUID

class BookQuizServiceTest :
	StringSpec({

		val insertBookQuizPort = mockk<InsertBookQuizPort>()
		val findCertificatedMemberUseCase = mockk<FindCertificatedMemberUseCase>()
		val bookQuizService = BookQuizService(insertBookQuizPort, findCertificatedMemberUseCase)

		"북 퀴즈를 생성한다" {
			every { findCertificatedMemberUseCase.getByCertificationId(any()) } returns memberResponseFixture()
			every { insertBookQuizPort.insert(any()) } returns 1
			bookQuizService.create(
				CreateBookQuizCommand(
					"title",
					"des",
					1,
					UUID.randomUUID(),
				),
			) shouldBe 1
		}
	})