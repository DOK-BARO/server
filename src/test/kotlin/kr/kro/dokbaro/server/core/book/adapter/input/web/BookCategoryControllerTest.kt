package kr.kro.dokbaro.server.core.book.adapter.input.web

import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.spring.SpringExtension
import io.mockk.every
import kr.kro.dokbaro.server.configuration.annotation.RestDocsTest
import kr.kro.dokbaro.server.configuration.docs.FieldType
import kr.kro.dokbaro.server.configuration.docs.Path
import kr.kro.dokbaro.server.configuration.docs.RestDocsExecutor
import kr.kro.dokbaro.server.core.book.application.port.input.CreateBookCategoryUseCase
import kr.kro.dokbaro.server.core.book.application.port.input.FindAllBookCategoryUseCase
import kr.kro.dokbaro.server.core.book.application.port.input.dto.CreateBookCategoryCommand
import kr.kro.dokbaro.server.core.book.query.BookCategoryTree
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpMethod
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@RestDocsTest
@WebMvcTest(BookCategoryController::class)
class BookCategoryControllerTest : StringSpec() {
	override fun extensions() = listOf(SpringExtension)

	@Autowired
	lateinit var mvc: MockMvc

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

			RestDocsExecutor(mvc, HttpMethod.GET, Path("/book-categories")) {
				given {
					parameters {
						"targetId" to "1"
					}
				}
				then {
					expect { status().isOk }
					queryParameters {
						"targetId" means "기준 ID" optional true
					}
					responseFields {
						"id" type FieldType.NUMBER means "ID"
						"name" type FieldType.STRING means "이름"
						"details" type FieldType.ARRAY(FieldType.OBJECT) means "세부 항목들"
					}
				}
			}
		}

		"책 카테고리 생성을 수행한다" {
			every { createBookCategoryUseCase.create(any(), any()) } returns 3

			RestDocsExecutor(mvc, HttpMethod.POST, Path("/book-categories")) {
				given {
					body {
						CreateBookCategoryCommand(
							koreanName = "모바일",
							englishName = "mobile",
							parentId = 1,
						)
					}
				}
				then {
					expect { status().isCreated }
					requestFields {
						"koreanName" type FieldType.STRING means "한국 이름"
						"englishName" type FieldType.STRING means "영어 이름"
						"parentId" type FieldType.NUMBER means "상위 카테고리 ID"
					}
					responseFields {
						"id" type FieldType.NUMBER means "saved ID"
					}
				}
			}
		}
	}
}