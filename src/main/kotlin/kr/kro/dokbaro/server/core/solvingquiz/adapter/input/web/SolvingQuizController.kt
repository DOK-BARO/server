package kr.kro.dokbaro.server.core.solvingquiz.adapter.input.web

import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/solving-quiz")
class SolvingQuizController {
	@PostMapping("/start")
	fun startSolvingQuiz() {
		TODO()
	}

	@PostMapping("/{id}/sheets")
	fun solveQuestion(
		@PathVariable id: String,
	) {
		TODO()
	}
}