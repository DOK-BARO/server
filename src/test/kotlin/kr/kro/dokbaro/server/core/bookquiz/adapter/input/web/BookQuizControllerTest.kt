package kr.kro.dokbaro.server.core.bookquiz.adapter.input.web

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import kr.kro.dokbaro.server.configuration.docs.Path
import kr.kro.dokbaro.server.configuration.docs.RestDocsTest
import kr.kro.dokbaro.server.core.bookquiz.adapter.input.web.dto.CreateBookQuizRequest
import kr.kro.dokbaro.server.core.bookquiz.adapter.input.web.dto.UpdateBookQuizRequest
import kr.kro.dokbaro.server.core.bookquiz.application.port.input.CreateBookQuizUseCase
import kr.kro.dokbaro.server.core.bookquiz.application.port.input.FindBookQuizQuestionUseCase
import kr.kro.dokbaro.server.core.bookquiz.application.port.input.UpdateBookQuizUseCase
import kr.kro.dokbaro.server.core.bookquiz.application.port.input.dto.CreateQuizQuestionCommand
import kr.kro.dokbaro.server.core.bookquiz.application.port.input.dto.UpdateQuizQuestionCommand
import kr.kro.dokbaro.server.core.bookquiz.domain.AccessScope
import kr.kro.dokbaro.server.core.bookquiz.domain.QuizType
import kr.kro.dokbaro.server.core.bookquiz.domain.SelectOption
import kr.kro.dokbaro.server.core.bookquiz.query.BookQuizQuestions
import kr.kro.dokbaro.server.core.bookquiz.query.Question
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(BookQuizController::class)
class BookQuizControllerTest : RestDocsTest() {
	@MockkBean
	lateinit var createBookQuizUseCase: CreateBookQuizUseCase

	@MockkBean
	lateinit var findBookQuizQuestionUseCase: FindBookQuizQuestionUseCase

	@MockkBean
	lateinit var updateBookQuizUseCase: UpdateBookQuizUseCase

	init {
		"북 퀴즈 생성을 수행한다" {
			every { createBookQuizUseCase.create(any()) } returns 1

			val body =
				CreateBookQuizRequest(
					title = "헥사고날 1장 요약",
					description = "문제가 쉬우니 모두 함께해요!",
					bookId = 56,
					questions =
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
							CreateQuizQuestionCommand(
								content = "명량에서 이순신 역은 류승룡이 담당했다",
								answerExplanation = "최민식이 담당했다",
								answerType = QuizType.OX,
								answers = listOf("X"),
							),
						),
					studyGroupId = 2,
					timeLimitSecond = 60,
					viewScope = AccessScope.EVERYONE,
					editScope = AccessScope.CREATOR,
				)

			performPost(Path("/book-quizzes"), body)
				.andExpect(status().isCreated)
				.andDo(
					print(
						"book-quiz/create-book-quiz",
						requestFields(
							fieldWithPath("title")
								.type(JsonFieldType.STRING)
								.description("퀴즈 제목"),
							fieldWithPath("description")
								.type(JsonFieldType.STRING)
								.description("퀴즈 설명"),
							fieldWithPath("bookId")
								.type(JsonFieldType.NUMBER)
								.description("퀴즈 관련 서적 ID"),
							fieldWithPath("timeLimitSecond")
								.type(JsonFieldType.NUMBER)
								.description("풀이 제한 시간 (optional), 무제한 시 null")
								.optional(),
							fieldWithPath("viewScope")
								.type(JsonFieldType.STRING)
								.description("보기 접근 권한 [EVERYONE, STUDY_GROUP, CREATOR]"),
							fieldWithPath("editScope")
								.type(JsonFieldType.STRING)
								.description("편집 접근 권한 [EVERYONE, STUDY_GROUP, CREATOR]"),
							fieldWithPath("questions[].content")
								.type(JsonFieldType.STRING)
								.description("질문 내용"),
							fieldWithPath("questions[].selectOptions")
								.type(JsonFieldType.ARRAY)
								.description("질문 선택지, 객관식에서만 사용, seq는 0부터 시작"),
							fieldWithPath("questions[].answerExplanation")
								.type(JsonFieldType.STRING)
								.description("답안 설명"),
							fieldWithPath("questions[].answerType")
								.type(JsonFieldType.STRING)
								.description("답안 타입 [OX, FILL_BLANK, MULTIPLE_CHOICE, SHORT]"),
							fieldWithPath("questions[].answers")
								.type(JsonFieldType.ARRAY)
								.description("답안"),
							fieldWithPath("studyGroupId")
								.type(JsonFieldType.NUMBER)
								.description("연관 Study group ID (optional)")
								.optional(),
						),
						responseFields(
							fieldWithPath("id")
								.type(JsonFieldType.NUMBER)
								.description("저장된 북퀴즈 ID"),
						),
					),
				)
		}

		"북 퀴즈 문제 조회를 수행한다" {
			every { findBookQuizQuestionUseCase.findBookQuizQuestionsBy(any()) } returns
				BookQuizQuestions(
					1,
					"java 정석 1차",
					60,
					listOf(
						Question(
							1,
							"조정석의 아내 이름은?",
							QuizType.MULTIPLE_CHOICE,
							listOf(
								SelectOption("거미"),
								SelectOption("개미"),
								SelectOption("고미"),
							),
						),
					),
				)

			performGet(Path("/book-quizzes/{id}/questions", "1"))
				.andExpect(status().isOk)
				.andDo(
					print(
						"book-quiz/get-book-quiz-questions",
						pathParameters(parameterWithName("id").description("퀴즈 ID")),
						responseFields(
							fieldWithPath("id").type(JsonFieldType.NUMBER).description("퀴즈의 고유 식별자").optional(),
							fieldWithPath("title").type(JsonFieldType.STRING).description("퀴즈 제목"),
							fieldWithPath("timeLimitSecond").type(JsonFieldType.NUMBER).description("퀴즈의 시간 제한(초), null 가능").optional(),
							fieldWithPath("questions").type(JsonFieldType.ARRAY).description("퀴즈에 포함된 질문 리스트"),
							fieldWithPath("questions[].id").type(JsonFieldType.NUMBER).description("질문의 고유 식별자"),
							fieldWithPath("questions[].content").type(JsonFieldType.STRING).description("질문의 내용"),
							fieldWithPath("questions[].selectOptions").type(JsonFieldType.ARRAY).description("질문의 선택지 리스트"),
							fieldWithPath("questions[].type")
								.type(JsonFieldType.STRING)
								.description("질문의 유형 [OX, FILL_BLANK, MULTIPLE_CHOICE, SHORT]"),
							fieldWithPath("questions[].selectOptions[].content").type(JsonFieldType.STRING).description("선택지의 내용"),
						),
					),
				)
		}

		"퀴즈를 수정한다" {
			every { updateBookQuizUseCase.update(any()) } returns Unit

			val body =
				UpdateBookQuizRequest(
					title = "헥사고날 1장 요약",
					description = "문제가 쉬우니 모두 함께해요!",
					bookId = 56,
					questions =
						listOf(
							UpdateQuizQuestionCommand(
								id = 1,
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
							UpdateQuizQuestionCommand(
								id = 2,
								content = "명량에서 이순신 역은 류승룡이 담당했다",
								answerExplanation = "최민식이 담당했다",
								answerType = QuizType.OX,
								answers = listOf("X"),
							),
						),
					studyGroupId = 2,
					timeLimitSecond = 60,
					viewScope = AccessScope.EVERYONE,
					editScope = AccessScope.CREATOR,
				)

			performPut(Path("/book-quizzes/{id}", "1"), body)
				.andExpect(status().isNoContent)
				.andDo(
					print(
						"book-quiz/update-book-quiz",
						pathParameters(parameterWithName("id").description("퀴즈 ID")),
						requestFields(
							fieldWithPath("title")
								.type(JsonFieldType.STRING)
								.description("퀴즈 제목"),
							fieldWithPath("description")
								.type(JsonFieldType.STRING)
								.description("퀴즈 설명"),
							fieldWithPath("bookId")
								.type(JsonFieldType.NUMBER)
								.description("퀴즈 관련 서적 ID"),
							fieldWithPath("timeLimitSecond")
								.type(JsonFieldType.NUMBER)
								.description("풀이 제한 시간 (optional), 무제한 시 null")
								.optional(),
							fieldWithPath("viewScope")
								.type(JsonFieldType.STRING)
								.description("보기 접근 권한 [EVERYONE, STUDY_GROUP, CREATOR]"),
							fieldWithPath("editScope")
								.type(JsonFieldType.STRING)
								.description("편집 접근 권한 [EVERYONE, STUDY_GROUP, CREATOR]"),
							fieldWithPath("questions[].id")
								.type(JsonFieldType.NUMBER)
								.description("questionId, 신규 api 인 경우 null 혹은 0")
								.optional(),
							fieldWithPath("questions[].content")
								.type(JsonFieldType.STRING)
								.description("질문 내용"),
							fieldWithPath("questions[].selectOptions")
								.type(JsonFieldType.ARRAY)
								.description("질문 선택지, 객관식에서만 사용, seq는 0부터 시작"),
							fieldWithPath("questions[].answerExplanation")
								.type(JsonFieldType.STRING)
								.description("답안 설명"),
							fieldWithPath("questions[].answerType")
								.type(JsonFieldType.STRING)
								.description("답안 타입 [OX, FILL_BLANK, MULTIPLE_CHOICE, SHORT]"),
							fieldWithPath("questions[].answers")
								.type(JsonFieldType.ARRAY)
								.description("답안"),
							fieldWithPath("studyGroupId")
								.type(JsonFieldType.NUMBER)
								.description("연관 Study group ID (optional)")
								.optional(),
						),
					),
				)
		}
	}
}