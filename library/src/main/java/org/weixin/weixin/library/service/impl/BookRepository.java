package org.weixin.weixin.library.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.weixin.weixin.library.domain.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, String> {

	Page<Book> findByDisabledFalse(Pageable pageable);

	// where name like '%' + keyword + '%' and disabled == false
	Page<Book> findByNameContainingAndDisabledFalse(String keyword, Pageable pageable);

}
