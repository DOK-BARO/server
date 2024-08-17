package kr.kro.dokbaro.server.core.book.adapter.input.web

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import kr.kro.dokbaro.server.configuration.docs.Path
import kr.kro.dokbaro.server.configuration.docs.RestDocsTest
import kr.kro.dokbaro.server.core.book.application.port.input.query.FindAllBookCategoryUseCase
import kr.kro.dokbaro.server.core.book.domain.BookCategory
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.queryParameters
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(BookCategoryController::class)
class BookCategoryControllerTest : RestDocsTest() {
	@MockkBean
	lateinit var fineAllBookCategoryUseCase: FindAllBookCategoryUseCase

	init {
		"책 카테고리 조회를 수행한다" {
			every { fineAllBookCategoryUseCase.findAllCategory(any()) } returns
				BookCategory(
					1,
					"IT",
					setOf(
						BookCategory(
							2,
							"운영체제",
							setOf(
								BookCategory(5, "우분투"),
								BookCategory(5, "유닉스"),
							),
						),
						BookCategory(3, "네트워크"),
						BookCategory(4, "개발 방법론"),
					),
				)

			val param = mapOf("targetId" to "1")

			performGet(Path("/book-categories"), param)
				.andExpect(status().isOk)
				.andDo(
					print(
						"book/find-book-category",
						queryParameters(
							parameterWithName("targetId").description("기준 ID (optional)").optional(),
						),
						responseFields(
							fieldWithPath("id").type(JsonFieldType.NUMBER).description("ID"),
							fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
							subsectionWithPath("details").type(JsonFieldType.ARRAY).description("세부 항목들"),
						),
					),
				)
		}
	}
}