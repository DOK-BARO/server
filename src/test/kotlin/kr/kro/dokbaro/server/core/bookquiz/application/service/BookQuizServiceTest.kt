package kr.kro.dokbaro.server.core.bookquiz.application.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kr.kro.dokbaro.server.core.bookquiz.application.port.input.dto.CreateBookQuizCommand
import kr.kro.dokbaro.server.core.bookquiz.application.port.input.dto.UpdateBookQuizCommand
import kr.kro.dokbaro.server.core.bookquiz.application.port.out.DeleteBookQuizPort
import kr.kro.dokbaro.server.core.bookquiz.application.port.out.InsertBookQuizPort
import kr.kro.dokbaro.server.core.bookquiz.application.port.out.LoadBookQuizByQuestionIdPort
import kr.kro.dokbaro.server.core.bookquiz.application.port.out.LoadBookQuizPort
import kr.kro.dokbaro.server.core.bookquiz.application.port.out.UpdateBookQuizPort
import kr.kro.dokbaro.server.core.bookquiz.application.service.exception.NotFoundQuizException
import kr.kro.dokbaro.server.core.bookquiz.domain.AccessScope
import kr.kro.dokbaro.server.core.bookquiz.domain.QuizType
import kr.kro.dokbaro.server.dummy.EventPublisherDummy
import kr.kro.dokbaro.server.fixture.domain.bookQuizFixture

class BookQuizServiceTest :
	StringSpec({

		val insertBookQuizPort = mockk<InsertBookQuizPort>()
		val loadBookQuizPort = mockk<LoadBookQuizPort>()
		val updateBookQuizPort = mockk<UpdateBookQuizPort>()
		val loadBookQuizByQuestionIdPort = mockk<LoadBookQuizByQuestionIdPort>()
		val deleteBookQuizPort = mockk<DeleteBookQuizPort>()

		val bookQuizService =
			BookQuizService(
				insertBookQuizPort,
				loadBookQuizPort,
				updateBookQuizPort,
				loadBookQuizByQuestionIdPort,
				deleteBookQuizPort,
				EventPublisherDummy(),
			)

		afterEach {
			clearAllMocks()
		}

		"북 퀴즈를 생성한다" {
			every { insertBookQuizPort.insert(any()) } returns 1
			bookQuizService.create(
				CreateBookQuizCommand(
					"title",
					"des",
					1,
					2,
					"creator",
					listOf(
						CreateBookQuizCommand.Question(
							"다음 중 천만 관객 영화가 아닌 것은?",
							listOf(
								"광해",
								"명량",
								"암살",
								"국제시장",
								"자전차왕 엄복동",
							),
							"엄복동은 누적 관객 수 17만명을 기록했다.",
							listOf("hello.png"),
							QuizType.MULTIPLE_CHOICE_MULTIPLE_ANSWER,
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
							UpdateBookQuizCommand.Question(
								id = 2,
								content = "명량에서 이순신 역은 류승룡이 담당했다",
								answerExplanationContent = "최민식이 담당했다",
								answerType = QuizType.OX,
								answers = listOf("X"),
							),
						),
					modifierId = 1,
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
								UpdateBookQuizCommand.Question(
									id = 2,
									content = "명량에서 이순신 역은 류승룡이 담당했다",
									answerExplanationContent = "최민식이 담당했다",
									answerType = QuizType.OX,
									answers = listOf("X"),
								),
							),
						modifierId = 1,
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
							UpdateBookQuizCommand.Question(
								content = "명량에서 이순신 역은 류승룡이 담당했다",
								answerExplanationContent = "최민식이 담당했다",
								answerType = QuizType.OX,
								answers = listOf("X"),
							),
						),
					modifierId = 1,
				),
			)

			verify { updateBookQuizPort.update(any()) }
		}

		"id를 통한 탐색을 수행한다" {
			every { loadBookQuizPort.load(any()) } returns bookQuizFixture()

			bookQuizService.findBy(1) shouldNotBe null
		}

		"id를 통한 탐색 수행 시 찾을 수 없다면 예외를 반환한다" {
			every { loadBookQuizPort.load(any()) } returns null

			shouldThrow<NotFoundQuizException> {
				bookQuizService.findBy(1)
			}
		}

		"question id를 통한 탐색을 수행한다" {
			every { loadBookQuizByQuestionIdPort.loadByQuestionId(any()) } returns bookQuizFixture()

			bookQuizService.findByQuestionId(1) shouldNotBe null
		}

		"question id를 통한 탐색 수행 시 찾을 수 없다면 예외를 반환한다" {
			every { loadBookQuizByQuestionIdPort.loadByQuestionId(any()) } returns null

			shouldThrow<NotFoundQuizException> {
				bookQuizService.findByQuestionId(1)
			}
		}

		"퀴즈 삭제를 수행한다" {
			every { deleteBookQuizPort.deleteBy(any()) } returns Unit

			bookQuizService.deleteBy(1)

			verify { deleteBookQuizPort.deleteBy(any()) }
		}
	})