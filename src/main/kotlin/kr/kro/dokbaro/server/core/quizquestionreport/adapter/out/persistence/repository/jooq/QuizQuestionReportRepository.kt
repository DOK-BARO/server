package kr.kro.dokbaro.server.core.quizquestionreport.adapter.out.persistence.repository.jooq

import kr.kro.dokbaro.server.core.quizquestionreport.domain.QuizQuestionReport
import org.jooq.DSLContext
import org.jooq.generated.tables.JQuizQuestionReport
import org.springframework.stereotype.Repository

@Repository
class QuizQuestionReportRepository(
	private val dslContext: DSLContext,
) {
	companion object {
		private val QUIZ_QUESTION_REPORT = JQuizQuestionReport.QUIZ_QUESTION_REPORT
	}

	fun insert(quizQuestionReport: QuizQuestionReport): Long =
		dslContext
			.insertInto(
				QUIZ_QUESTION_REPORT,
				QUIZ_QUESTION_REPORT.QUIZ_QUESTION_ID,
				QUIZ_QUESTION_REPORT.REPORT_MEMBER_ID,
				QUIZ_QUESTION_REPORT.CONTENT,
			).values(
				quizQuestionReport.questionId,
				quizQuestionReport.reporterId,
				quizQuestionReport.content,
			).returningResult(QUIZ_QUESTION_REPORT.ID)
			.fetchOneInto(Long::class.java)!!
}