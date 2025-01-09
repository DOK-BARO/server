package kr.kro.dokbaro.server.core.quizquestionreport.adapter.out.persistence.repository.jooq

import kr.kro.dokbaro.server.core.quizquestionreport.domain.QuizQuestionReport
import org.jooq.DSLContext
import org.jooq.generated.tables.JQuizQuestionReport
import org.jooq.generated.tables.JQuizQuestionReportContent
import org.springframework.stereotype.Repository

@Repository
class QuizQuestionReportRepository(
	private val dslContext: DSLContext,
) {
	companion object {
		private val QUIZ_QUESTION_REPORT = JQuizQuestionReport.QUIZ_QUESTION_REPORT
		private val QUIZ_QUESTION_REPORT_CONTENT = JQuizQuestionReportContent.QUIZ_QUESTION_REPORT_CONTENT
	}

	fun insert(quizQuestionReport: QuizQuestionReport): Long {
		val reportId: Long =
			dslContext
				.insertInto(
					QUIZ_QUESTION_REPORT,
					QUIZ_QUESTION_REPORT.QUIZ_QUESTION_ID,
					QUIZ_QUESTION_REPORT.REPORT_MEMBER_ID,
				).values(
					quizQuestionReport.questionId,
					quizQuestionReport.reporterId,
				).returningResult(QUIZ_QUESTION_REPORT.ID)
				.fetchOneInto(Long::class.java)!!

		val contentInsertQuery =
			quizQuestionReport.contents.map {
				dslContext
					.insertInto(
						QUIZ_QUESTION_REPORT_CONTENT,
						QUIZ_QUESTION_REPORT_CONTENT.QUIZ_QUESTION_REPORT_ID,
						QUIZ_QUESTION_REPORT_CONTENT.CONTENT,
					).values(
						reportId,
						it,
					)
			}

		dslContext.batch(contentInsertQuery).execute()

		return reportId
	}
}