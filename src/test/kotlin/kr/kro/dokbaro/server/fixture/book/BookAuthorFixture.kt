package kr.kro.dokbaro.server.fixture.book

import org.jooq.generated.tables.pojos.BookAuthor

enum class BookAuthorFixture(
	val id: Long,
	val bookId: Long,
	val authorName: String,
) {
	JOSHUA_BLOCH(
		1L,
		1L,
		"Joshua Bloch",
	),
	ROBERT_C_MARTIN_CLEAN_CODE(
		2L,
		2L,
		"Robert C. Martin",
	),
	MARTIN_KLEPPMANN(
		3L,
		3L,
		"Martin Kleppmann",
	),
	ERICH_GAMMA(
		4L,
		4L,
		"Erich Gamma",
	),
	ROBERT_C_MARTIN_AGILE_SOFTWARE(
		5L,
		5L,
		"Robert C. Martin",
	),
	ROBERT_C_MARTIN_CLEAN_ARCHITECTURE(
		6L,
		6L,
		"Robert C. Martin",
	),
	MARTIN_FOWLER(
		7L,
		7L,
		"Martin Fowler",
	),
	ANDREW_HUNT(
		8L,
		8L,
		"Andrew Hunt",
	),
	ERIC_FREEMAN(
		9L,
		9L,
		"Eric Freeman",
	),
	JEZ_HUMBLE(
		10L,
		10L,
		"Jez Humble",
	),
	THOMAS_H_CORMEN(
		11L,
		11L,
		"Thomas H. Cormen",
	),
	FREDERICK_P_BROOKS_JR(
		12L,
		12L,
		"Frederick P. Brooks Jr.",
	),
	BRIAN_W_KERNIGHAN(
		13L,
		13L,
		"Brian W. Kernighan",
	),
	;

	fun toJooqBookAuthor() = BookAuthor(id, bookId, authorName)
}