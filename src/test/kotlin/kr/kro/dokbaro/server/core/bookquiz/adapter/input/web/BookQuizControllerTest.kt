package kr.kro.dokbaro.server.core.bookquiz.adapter.input.web

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import kr.kro.dokbaro.server.configuration.docs.Path
import kr.kro.dokbaro.server.configuration.docs.RestDocsTest
import kr.kro.dokbaro.server.core.bookquiz.adapter.input.web.dto.CreateBookQuizRequest
import kr.kro.dokbaro.server.core.bookquiz.application.port.input.CreateBookQuizUseCase
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(BookQuizController::class)
class BookQuizControllerTest : RestDocsTest() {
	@MockkBean
	lateinit var createBookQuizUseCase: CreateBookQuizUseCase

	init {
		"북 퀴즈 생성을 수행한다" {
			every { createBookQuizUseCase.create(any()) } returns 1

			val body =
				CreateBookQuizRequest(
					title = "헥사고날 1장 요약",
					description = "문제가 쉬우니 모두 함께해요!",
					bookId = 56,
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
						),
						responseFields(
							fieldWithPath("id")
								.type(JsonFieldType.NUMBER)
								.description("저장된 북퀴즈 ID"),
						),
					),
				)
		}
	}
}