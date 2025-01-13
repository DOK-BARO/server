package kr.kro.dokbaro.server.core.quizreviewreport.adapter.out.persistence.repository.jooq

import kr.kro.dokbaro.server.core.quizreviewreport.domain.QuizReviewReport
import org.jooq.DSLContext
import org.jooq.generated.tables.JQuizReviewReport.QUIZ_REVIEW_REPORT
import org.springframework.stereotype.Repository

@Repository
class QuizReviewReportRepository(
	private val dslContext: DSLContext,
) {
	fun insert(quizReviewReport: QuizReviewReport): Long =
		dslContext
			.insertInto(
				QUIZ_REVIEW_REPORT,
				QUIZ_REVIEW_REPORT.QUIZ_REVIEW_ID,
				QUIZ_REVIEW_REPORT.REPORT_MEMBER_ID,
				QUIZ_REVIEW_REPORT.CONTENT,
			).values(
				quizReviewReport.quizReviewId,
				quizReviewReport.reporterId,
				quizReviewReport.content,
			).returningResult(QUIZ_REVIEW_REPORT.ID)
			.fetchOneInto(Long::class.java)!!
}