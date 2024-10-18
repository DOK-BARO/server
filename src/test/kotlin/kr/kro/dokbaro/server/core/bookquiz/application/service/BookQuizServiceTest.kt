package kr.kro.dokbaro.server.core.bookquiz.application.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kr.kro.dokbaro.server.core.bookquiz.application.port.input.dto.CreateBookQuizCommand
import kr.kro.dokbaro.server.core.bookquiz.application.port.input.dto.CreateQuizQuestionCommand
import kr.kro.dokbaro.server.core.bookquiz.application.port.input.dto.UpdateBookQuizCommand
import kr.kro.dokbaro.server.core.bookquiz.application.port.input.dto.UpdateQuizQuestionCommand
import kr.kro.dokbaro.server.core.bookquiz.application.port.out.InsertBookQuizPort
import kr.kro.dokbaro.server.core.bookquiz.application.port.out.LoadBookQuizPort
import kr.kro.dokbaro.server.core.bookquiz.application.port.out.UpdateBookQuizPort
import kr.kro.dokbaro.server.core.bookquiz.application.service.exception.NotFoundQuizException
import kr.kro.dokbaro.server.core.bookquiz.domain.AccessScope
import kr.kro.dokbaro.server.core.bookquiz.domain.QuizType
import kr.kro.dokbaro.server.core.member.application.port.input.query.FindCertificatedMemberUseCase
import kr.kro.dokbaro.server.fixture.domain.bookQuizFixture
import kr.kro.dokbaro.server.fixture.domain.memberResponseFixture
import java.util.UUID

class BookQuizServiceTest :
	StringSpec({

		val insertBookQuizPort = mockk<InsertBookQuizPort>()
		val findCertificatedMemberUseCase = mockk<FindCertificatedMemberUseCase>()
		val loadBookQuizPort = mockk<LoadBookQuizPort>()
		val updateBookQuizPort = mockk<UpdateBookQuizPort>()

		val bookQuizService =
			BookQuizService(
				insertBookQuizPort,
				findCertificatedMemberUseCase,
				loadBookQuizPort,
				updateBookQuizPort,
			)

		afterEach {
			clearAllMocks()
		}

		"북 퀴즈를 생성한다" {
			every { findCertificatedMemberUseCase.getByCertificationId(any()) } returns memberResponseFixture()
			every { insertBookQuizPort.insert(any()) } returns 1
			bookQuizService.create(
				CreateBookQuizCommand(
					"title",
					"des",
					1,
					UUID.randomUUID(),
					listOf(
						CreateQuizQuestionCommand(
							"다음 중 천만 관객 영화가 아닌 것은?",
							listOf(
								"광해",
								"명량",
								"암살",
								"국제시장",
								"자전차왕 엄복동",
							),
							"엄복동은 누적 관객 수 17만명을 기록했다.",
							QuizType.MULTIPLE_CHOICE,
							listOf("4"),
						),
					),
					timeLimitSecond = 60,
					viewScope = AccessScope.EVERYONE,
					editScope = AccessScope.CREATOR,
				),
			) shouldBe 1
		}

		"book quiz를 수정한다" {
			every { loadBookQuizPort.load(any()) } returns bookQuizFixture()

			every { updateBookQuizPort.update(any()) } returns Unit

			bookQuizService.update(
				UpdateBookQuizCommand(
					id = 1,
					title = "newtitle",
					description = "newdescription",
					bookId = 2,
					timeLimitSecond = 4,
					viewScope = AccessScope.EVERYONE,
					editScope = AccessScope.CREATOR,
					studyGroupId = null,
					questions =
						listOf(
							UpdateQuizQuestionCommand(
								id = 2,
								content = "명량에서 이순신 역은 류승룡이 담당했다",
								answerExplanation = "최민식이 담당했다",
								answerType = QuizType.OX,
								answers = listOf("X"),
							),
						),
				),
			)

			verify { updateBookQuizPort.update(any()) }
		}

		"book quiz 수정 시 id에 해당하는 값을 찾을 수 없으면 예외를 반환한다" {
			every { loadBookQuizPort.load(any()) } returns null

			shouldThrow<NotFoundQuizException> {
				bookQuizService.update(
					UpdateBookQuizCommand(
						id = 1,
						title = "newtitle",
						description = "newdescription",
						bookId = 2,
						timeLimitSecond = 4,
						viewScope = AccessScope.EVERYONE,
						editScope = AccessScope.CREATOR,
						studyGroupId = null,
						questions =
							listOf(
								UpdateQuizQuestionCommand(
									id = 2,
									content = "명량에서 이순신 역은 류승룡이 담당했다",
									answerExplanation = "최민식이 담당했다",
									answerType = QuizType.OX,
									answers = listOf("X"),
								),
							),
					),
				)
			}
		}

		"book quiz 수정 시 question id가 없으면 (신규 question 이면) id를 0으로 대체한다" {
			every { loadBookQuizPort.load(any()) } returns bookQuizFixture()

			every { updateBookQuizPort.update(any()) } returns Unit

			bookQuizService.update(
				UpdateBookQuizCommand(
					id = 1,
					title = "newtitle",
					description = "newdescription",
					bookId = 2,
					timeLimitSecond = 4,
					viewScope = AccessScope.EVERYONE,
					editScope = AccessScope.CREATOR,
					studyGroupId = null,
					questions =
						listOf(
							UpdateQuizQuestionCommand(
								content = "명량에서 이순신 역은 류승룡이 담당했다",
								answerExplanation = "최민식이 담당했다",
								answerType = QuizType.OX,
								answers = listOf("X"),
							),
						),
				),
			)

			verify { updateBookQuizPort.update(any()) }
		}
	})