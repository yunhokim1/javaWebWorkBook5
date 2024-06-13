package org.zerock.b01.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.b01.dto.BoardDTO;
import org.zerock.b01.dto.PageRequestDTO;
import org.zerock.b01.dto.PageResponseDTO;

@SpringBootTest
@Slf4j
public class BoardServiceTests {

    @Autowired
    private BoardService boardService;

    @Test
    void testRegister() {

        log.info(boardService.getClass().getName());

        BoardDTO boardDTO = BoardDTO.builder()
                .title("Sample title...")
                .content("Sample Content...")
                .writer("user00")
                .build();

        Long bno = boardService.register(boardDTO);
        log.info("bno={}", bno);
    }

    @Test
    void testReadOne() {

        Long bno = 100L;

        BoardDTO boardDTO = boardService.readOne(bno);
        log.info(String.valueOf(boardDTO));

    }

    @Test
    void testModify() {

        BoardDTO boardDTO = BoardDTO.builder()
                .bno(102L)
                .title("Modify Title...")
                .content("Modify Content...")
                .build();

        boardService.modify(boardDTO);
    }

    @Test
    void testList() {

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .type("tcw")
                .keyword("0")
                .build();

        PageResponseDTO<BoardDTO> responseDTO = boardService.list(pageRequestDTO);
        log.info(String.valueOf(responseDTO));
    }
}
