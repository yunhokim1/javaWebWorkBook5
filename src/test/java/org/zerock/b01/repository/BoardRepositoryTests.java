package org.zerock.b01.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.b01.domain.Board;
import org.zerock.b01.domain.BoardImage;
import org.zerock.b01.dto.BoardListAllDTO;
import org.zerock.b01.dto.BoardListReplyCountDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class BoardRepositoryTests {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private ReplyRepository replyRepository;

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

    @Test
    void testInsertWithImages() {
        
        Board board = Board.builder()
                .title("제목")
                .content("내용")
                .writer("작성자")
                .build();

        for (int i = 0; i < 3; i++) {
            board.addImage(UUID.randomUUID().toString(), "file" + i + ".jpg");
        }

        boardRepository.save(board);

    }

    @Test
    void testReadWithImages() {

        Optional<Board> result = boardRepository.findByIdWithImages(1L);
        Board board = result.orElseThrow();

        log.info(board);
        log.info("------------------------");
        for (BoardImage boardImage : board.getImageSet()) {
            log.info(boardImage);
        }
    }

    @Test
    @Transactional
    @Commit
    void testModifyImage() {

        Optional<Board> result = boardRepository.findByIdWithImages(1L);
        Board board = result.orElseThrow();

        //기존의 첨부파일들은 삭제
        board.clearImages();

        //새로운 첨부 파일들
        for (int i = 0; i < 2; i++) {
            board.addImage(UUID.randomUUID().toString(), "updateFile" + i + ".jpg");
        }

        boardRepository.save(board);
    }

    @Test
    @Transactional
    @Commit
    void testRemoveAll() {

        Long bno = 1L;
        replyRepository.deleteByBoard_Bno(bno);
        boardRepository.deleteById(bno);
    }

    @Test
    void testInsertAll() {

        for (int i = 1; i <= 100; i++) {

            Board board = Board.builder()
                    .title("Title..." + i)
                    .content("Content..." + i)
                    .writer("Writer..." + i)
                    .build();

            for (int j = 0; j < 3; j++) {
                if (i % 5 == 0) {
                    continue;
                }
                board.addImage(UUID.randomUUID().toString(), "file" + i + ".jpg");
            }
            boardRepository.save(board);
        }
    }

    @Test
    @Transactional
    void testSearchImageReplyCount() {

        PageRequest pageable = PageRequest.of(0, 10, Sort.by("bno").descending());
        Page<BoardListAllDTO> result = boardRepository.searchWithAll(null, null, pageable);

        log.info("--------------------------");
        log.info(result.getTotalElements());

        result.getContent().forEach(boardListAllDTO -> log.info(boardListAllDTO));

    }

}
