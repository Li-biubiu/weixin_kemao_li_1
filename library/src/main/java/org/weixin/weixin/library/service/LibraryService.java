package org.weixin.weixin.library.service;

import org.springframework.data.domain.Page;
import org.weixin.weixin.library.domain.Book;
import org.weixin.weixin.library.domain.DebitList;

public interface LibraryService {

	Page<Book> search(String keyword, int pageNumber);

	void add(String id, DebitList list);

	void remove(String id, DebitList list);

}
