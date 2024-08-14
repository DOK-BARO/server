package kr.kro.dokbaro.server.fixture.book

import org.jooq.generated.tables.pojos.BookCategoryGroup

enum class BookCategoryGroupFixture(
	private val bookId: Long,
	private val categoryId: Long,
) {
	A(1, 5),
	B(1, 12),
	C(1, 34),
	D(2, 7),
	E(2, 18),
	F(3, 21),
	G(3, 45),
	H(4, 9),
	I(4, 30),
	J(5, 25),
	K(6, 11),
	L(7, 22),
	M(8, 55),
	N(9, 14),
	O(10, 3),
	P(11, 40),
	Q(12, 29),
	R(13, 63),
	;

	fun toJooqBookCategoryGroup() = BookCategoryGroup(ordinal.toLong() + 1, bookId, categoryId)
}