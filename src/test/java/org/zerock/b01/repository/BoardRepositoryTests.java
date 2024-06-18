package org.zerock.b01.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.zerock.b01.domain.Board;
import org.zerock.b01.dto.BoardListReplyCountDTO;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class BoardRepositoryTests {

    @Autowired
    private BoardRepository boardRepository;

    @Test
    void testInsert() {

        IntStream.rangeClosed(1,100).forEach(i -> {
            Board board = Board.builder()
                    .title("title..." + i)
                    .content("content..." + i)
                    .writer("user" + (i % 10))
                    .build();

            Board result = boardRepository.save(board);
            log.info("BNO: " + result.getBno());
        });

    }

    @Test
    void testSelect() {

        Long bno = 100L;

        Optional<Board> result = boardRepository.findById(bno);

        Board board = result.orElseThrow();

        log.info(String.valueOf(board));

    }

    @Test
    void testUpdate() {
        Long bno = 100L;

        Optional<Board> result = boardRepository.findById(bno);
        Board board = result.orElseThrow();
        board.change("update title...100", "update content...100");

        boardRepository.save(board);
    }

    @Test
    void testDelete() {

        Long bno = 1L;

        boardRepository.deleteById(bno);
    }

    @Test
    void testPaging() {

        //1page order by bno desc
        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());

        Page<Board> result = boardRepository.findAll(pageable);

        log.info("total count: {}", result.getTotalElements());
        log.info("total pages: {}", result.getTotalPages());
        log.info("page number: {}", result.getNumber());
        log.info("page size: {}", result.getSize());

        List<Board> todoList = result.getContent();

        todoList.forEach(board -> log.info(board));
    }

    @Test
    void testSearch1() {

        //2page order by bno desc
        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());

        boardRepository.search1(pageable);
    }

    @Test
    void testSearchAll() {

        String[] types = {"t", "c", "w"};
        String keyword = "1";

        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());

        Page<Board> result = boardRepository.searchAll(types, keyword, pageable);
    }

    @Test
    void testSearchAll2() {

        String[] types = {"t", "c", "w"};
        String keyword = "1";

        PageRequest pageable = PageRequest.of(0, 10, Sort.by("bno").descending());

        Page<Board> result = boardRepository.searchAll(types, keyword, pageable);

        //total pages
        log.info(result.getTotalPages());

        //page size
        log.info(result.getSize());

        //page number
        log.info(result.getNumber());

        //prev next
        log.info(result.hasPrevious() + ":" + result.hasNext());

        result.getContent().forEach(board -> log.info(board));

    }

    @Test
    void testSearchReplyCount() {

        String[] types = {"t", "c", "w"};
        String keyword = "1";
        PageRequest pageable = PageRequest.of(0, 10, Sort.by("bno").descending());

        Page<BoardListReplyCountDTO> result = boardRepository.searchWithReplyCount(types, keyword, pageable);

        //total pages
        log.info(result.getTotalPages());

        //page size
        log.info(result.getSize());

        //page number
        log.info(result.getNumber());

        //prev next
        log.info(result.hasPrevious() + ":" + result.hasNext());

        result.getContent().forEach(board -> log.info(board));
    }
}
