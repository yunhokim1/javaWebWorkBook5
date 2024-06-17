package org.zerock.b01.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.zerock.b01.domain.Board;
import org.zerock.b01.domain.Reply;

@SpringBootTest
@Slf4j
public class ReplyRepositoryTests {

    @Autowired
    ReplyRepository replyRepository;

    @Test
    void testInsert() {
        Long bno = 100L;

        Board board = Board.builder().bno(bno).build();
        Reply reply = Reply.builder()
                .board(board)
                .replyText("댓글......")
                .replyer("replyer1")
                .build();
        replyRepository.save(reply);
    }

    @Test
    void testBoardReplies() {
        Long bno = 100L;
        PageRequest pageable = PageRequest.of(0, 10, Sort.by("rno").descending());
        Page<Reply> result = replyRepository.listOfBoard(bno, pageable);
        result.getContent().forEach(reply -> {
            log.info(String.valueOf(reply));
        });
    }
}
