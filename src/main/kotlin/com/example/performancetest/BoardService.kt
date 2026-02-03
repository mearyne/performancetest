package com.example.performancetest

import org.springframework.stereotype.Service

@Service
class BoardService(
    private val boardRepository: BoardRepository, // 의존성 주입
) {
    fun create(
        boardDto: BoardDto
    ):BoardDto {
        val boardEntity = BoardEntity(
            title = boardDto.title,
            content = boardDto.content,
            name = boardDto.name,
        )

        val savedBoardEntity = boardRepository.save(boardEntity)
        return BoardDto(
            id = savedBoardEntity.id,
            title = savedBoardEntity.title,
            content = savedBoardEntity.content,
            name = savedBoardEntity.name
        )
    }

    fun getAll(): List<BoardDto> {
        val boardEntities = boardRepository.findAll()

        val boardDtoList = boardEntities.map { board -> // for문
            BoardDto(
                id = board.id,
                title = board.title,
                content = board.content,
                name = board.name,
            )
        }
        return boardDtoList
    }

    fun getById(
        id: Long,
    ): BoardDto {
        val boardEntity = boardRepository.findById(id).get()
        val boardDto = BoardDto(
            id = boardEntity.id,
            title = boardEntity.title,
            content = boardEntity.content,
            name = boardEntity.name,
        )
        return boardDto
    }

    fun update(boardDto: BoardDto) {
        val id = boardDto.id!! // 절대 null이 아니다
        val boardEntity = boardRepository.findById(id).get()

        boardEntity.update(boardDto)

        boardRepository.save(boardEntity)
    }

    fun deleteById(id: Long) {
        boardRepository.deleteById(id)
    }
}