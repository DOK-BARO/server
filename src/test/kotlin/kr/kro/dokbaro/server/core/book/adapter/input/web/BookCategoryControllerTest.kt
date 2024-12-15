package kr.kro.dokbaro.server.core.book.adapter.input.web

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import kr.kro.dokbaro.server.configuration.docs.Path
import kr.kro.dokbaro.server.configuration.docs.RestDocsTest
import kr.kro.dokbaro.server.core.book.application.port.input.CreateBookCategoryUseCase
import kr.kro.dokbaro.server.core.book.application.port.input.FindAllBookCategoryUseCase
import kr.kro.dokbaro.server.core.book.application.port.input.dto.CreateBookCategoryCommand
import kr.kro.dokbaro.server.core.book.query.BookCategoryTree
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.queryParameters
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(BookCategoryController::class)
class BookCategoryControllerTest : RestDocsTest() {
	@MockkBean
	lateinit var createBookCategoryUseCase: CreateBookCategoryUseCase

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
								BookCategoryTree(5, "우분투"),
								BookCategoryTree(5, "유닉스"),
							),
						),
						BookCategoryTree(3, "네트워크"),
						BookCategoryTree(4, "개발 방법론"),
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

		"책 카테고리 생성을 수행한다" {
			every { createBookCategoryUseCase.create(any(), any()) } returns 3

			val command = CreateBookCategoryCommand("모바일", "mobile", 1)

			performPost(Path("/book-categories"), command)
				.andExpect(status().isCreated)
				.andDo(
					print(
						"book/create-book-category",
						requestFields(
							fieldWithPath("koreanName").type(JsonFieldType.STRING).description("한국 이름"),
							fieldWithPath("englishName").type(JsonFieldType.STRING).description("영어 이름"),
							fieldWithPath("parentId").type(JsonFieldType.NUMBER).description("상위 카테고리 ID"),
						),
						responseFields(
							fieldWithPath("id").type(JsonFieldType.NUMBER).description("saved ID"),
						),
					),
				)
		}
	}
}