package kr.kro.dokbaro.server.core.book.adapter.input.web

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import kr.kro.dokbaro.server.configuration.docs.Path
import kr.kro.dokbaro.server.configuration.docs.RestDocsTest
import kr.kro.dokbaro.server.core.book.application.port.input.FindAllBookCategoryUseCase
import kr.kro.dokbaro.server.core.book.query.BookCategoryTree
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
			every { fineAllBookCategoryUseCase.getTree(any()) } returns
				BookCategoryTree(
					1,
					"IT",
					setOf(
						BookCategoryTree(
							2,
							"운영체제",
							setOf(
								BookCategoryTree(5, "우분투", null),
								BookCategoryTree(5, "유닉스", null),
							),
						),
						BookCategoryTree(3, "네트워크", null),
						BookCategoryTree(4, "개발 방법론", null),
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