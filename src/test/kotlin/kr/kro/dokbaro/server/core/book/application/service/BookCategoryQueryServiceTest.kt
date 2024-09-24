package kr.kro.dokbaro.server.core.book.application.service

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import kr.kro.dokbaro.server.core.book.application.port.out.ReadBookCategoryPort
import kr.kro.dokbaro.server.core.book.query.BookCategoryTree

class BookCategoryQueryServiceTest :
	StringSpec({
		val readBookCategoryPort = mockk<ReadBookCategoryPort>()
		val service = BookCategoryQueryService(readBookCategoryPort)

		"category tree 를 조회한다" {
			every { readBookCategoryPort.findTreeBy(any()) } returns
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

			service.getTree(1) shouldNotBe null
		}

		"id가 null이면 root 로 조회한다" {
			every { readBookCategoryPort.findTreeBy(any()) } returns
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

			service.getTree(null) shouldNotBe null
		}
	})