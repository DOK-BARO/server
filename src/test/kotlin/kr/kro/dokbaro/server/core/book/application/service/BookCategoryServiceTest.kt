package kr.kro.dokbaro.server.core.book.application.service

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kr.kro.dokbaro.server.core.book.application.port.out.LoadBookCategoryPort
import kr.kro.dokbaro.server.core.book.domain.BookCategory

class BookCategoryServiceTest :
	StringSpec({
		val loadBookCategoryPort = mockk<LoadBookCategoryPort>()

		val bookCategoryService = BookCategoryService(loadBookCategoryPort)

		val example =
			BookCategory(
				1,
				"root",
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

		"카테고리를 찾는다" {
			every { loadBookCategoryPort.getBookCategory(any()) } returns example

			bookCategoryService.findAllCategory(1) shouldBe example
		}

		"만약 id값이 명시되지 않으면 ROOT ID를 기준으로 탐색한다" {
			every { loadBookCategoryPort.getBookCategory(BookCategory.ROOT_ID) } returns example

			bookCategoryService.findAllCategory(null) shouldBe example
		}
	})