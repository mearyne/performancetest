package com.example.performancetest

import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/board")
class BoardController(
    private val boardService: BoardService
) {

    @PostMapping
    fun create(
        @RequestBody boardDto: BoardDto
    ):BoardDto {
        return boardService.create(boardDto)
    }

    @GetMapping("/{id}")
    fun get(
        @PathVariable id: Long
    ): BoardDto {
        return boardService.getById(id)
    }

    @GetMapping
    fun getAll(): List<BoardDto> {
        return boardService.getAll()
    }

    @PatchMapping
    fun update(
        @RequestBody boardDto: BoardDto
    ) {
        boardService.update(boardDto)
    }

    @DeleteMapping("/{id}")
    fun deleteById(
        @PathVariable id: Long
    ) {
        boardService.deleteById(id)
    }


}