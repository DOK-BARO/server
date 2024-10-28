package kr.kro.dokbaro.server.core.solvingquiz.adapter.input.web

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import kr.kro.dokbaro.server.configuration.docs.Path
import kr.kro.dokbaro.server.configuration.docs.RestDocsTest
import kr.kro.dokbaro.server.core.solvingquiz.adapter.input.web.dto.SolveQuestionRequest
import kr.kro.dokbaro.server.core.solvingquiz.adapter.input.web.dto.StartSolvingQuizRequest
import kr.kro.dokbaro.server.core.solvingquiz.application.port.input.FindAllSolveResultUseCase
import kr.kro.dokbaro.server.core.solvingquiz.application.port.input.SolveQuestionUseCase
import kr.kro.dokbaro.server.core.solvingquiz.application.port.input.StartSolvingQuizUseCase
import kr.kro.dokbaro.server.core.solvingquiz.query.SolveResult
import kr.kro.dokbaro.server.core.solvingquiz.query.TotalGradeResult
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(SolvingQuizController::class)
class SolvingQuizControllerTest : RestDocsTest() {
	@MockkBean
	lateinit var startSolvingQuizUseCase: StartSolvingQuizUseCase

	@MockkBean
	lateinit var solveQuestionUseCase: SolveQuestionUseCase

	@MockkBean
	lateinit var findAllSolveResultUseCase: FindAllSolveResultUseCase

	init {
		"퀴즈 풀기를 시작한다" {
			every { startSolvingQuizUseCase.start(any()) } returns 1

			val body = StartSolvingQuizRequest(1)

			performPost(Path("/solving-quiz/start"), body)
				.andExpect(status().isOk)
				.andDo(
					print(
						"solving-quiz/solving-quiz-start",
						requestFields(
							fieldWithPath("quizId").type(JsonFieldType.NUMBER).description("quiz id"),
						),
						responseFields(
							fieldWithPath("id").type(JsonFieldType.NUMBER).description("solving quiz id"),
						),
					),
				)
		}

		"퀴즈 문제에 대한 답을 체출한다" {
			every { solveQuestionUseCase.solve(any()) } returns
				SolveResult(
					solvingQuizId = 1L,
					playerId = 101L,
					quizId = 1001L,
					questionId = 10001L,
					correct = true,
					correctAnswer = listOf("A", "B"),
					answerExplanationContent = "The correct answers are A and B because...",
					answerExplanationImages = listOf("image1.jpg", "image2.jpg"),
				)

			val body = SolveQuestionRequest(1, listOf("홍길동, 가나다"))

			performPost(Path("/solving-quiz/{id}/sheets", "1"), body)
				.andExpect(status().isOk)
				.andDo(
					print(
						"solving-quiz/submit-quiz-sheet",
						pathParameters(
							parameterWithName("id").description("solving quiz id"),
						),
						requestFields(
							fieldWithPath("questionId").type(JsonFieldType.NUMBER).description("질문 id"),
							fieldWithPath("answers").type(JsonFieldType.ARRAY).description("답안"),
						),
						responseFields(
							fieldWithPath("solvingQuizId").type(JsonFieldType.NUMBER).description("현재 풀고 있는 퀴즈의 고유 ID."),
							fieldWithPath("playerId").type(JsonFieldType.NUMBER).description("플레이어의 ID."),
							fieldWithPath("quizId").type(JsonFieldType.NUMBER).description("퀴즈의 ID."),
							fieldWithPath("questionId").type(JsonFieldType.NUMBER).description("질문의 ID."),
							fieldWithPath("correct").type(JsonFieldType.BOOLEAN).description("정답 여부. 정답이면 true, 오답이면 false."),
							fieldWithPath("correctAnswer").type(JsonFieldType.ARRAY).description("정답으로 인정되는 답안의 목록."),
							fieldWithPath("answerExplanationContent").type(JsonFieldType.STRING).description("정답에 대한 설명 내용."),
							fieldWithPath("answerExplanationImages").type(JsonFieldType.ARRAY).description("정답 설명에 포함된 이미지 URL 목록."),
						),
					),
				)
		}

		"전체 채점 결과를 조회한다" {
			every { findAllSolveResultUseCase.findAllBy(any()) } returns
				TotalGradeResult(1, 1, 1, 10, 8)

			performGet(Path("/solving-quiz/{id}/grade-result", "1"))
				.andExpect(status().isOk)
				.andDo(
					print(
						"solving-quiz/grade-result",
						pathParameters(parameterWithName("id").description("문제 풀이 ID")),
						responseFields(
							fieldWithPath("solvingQuizId").type(JsonFieldType.NUMBER).description("풀고 있는 퀴즈의 고유 ID."),
							fieldWithPath("quizId").type(JsonFieldType.NUMBER).description("퀴즈의 ID."),
							fieldWithPath("playerId").type(JsonFieldType.NUMBER).description("플레이어의 ID."),
							fieldWithPath("questionCount").type(JsonFieldType.NUMBER).description("퀴즈에 포함된 질문의 총 개수."),
							fieldWithPath("correctCount").type(JsonFieldType.NUMBER).description("플레이어가 맞힌 정답의 개수."),
						),
					),
				)
		}
	}
}