package kr.kro.dokbaro.server.core.solvingquiz.adapter.input.web

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import kr.kro.dokbaro.server.configuration.docs.Path
import kr.kro.dokbaro.server.configuration.docs.RestDocsTest
import kr.kro.dokbaro.server.core.solvingquiz.adapter.input.web.dto.SolveQuestionRequest
import kr.kro.dokbaro.server.core.solvingquiz.adapter.input.web.dto.StartSolvingQuizRequest
import kr.kro.dokbaro.server.core.solvingquiz.application.port.input.SolveQuestionUseCase
import kr.kro.dokbaro.server.core.solvingquiz.application.port.input.StartSolvingQuizUseCase
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
			every { solveQuestionUseCase.solve(any()) } returns Unit

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
					),
				)
		}
	}
}