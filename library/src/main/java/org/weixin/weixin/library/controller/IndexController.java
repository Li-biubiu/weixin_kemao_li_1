package org.weixin.weixin.library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.weixin.weixin.library.domain.Book;
import org.weixin.weixin.library.service.LibraryService;

@Controller
@RequestMapping("/ljh_1/library")
public class IndexController {
	
	@Autowired
	private LibraryService libraryService;
	
	@RequestMapping
	public String index(
			Model model,
			// required表示必须的，如果为false则非必须的
			@RequestParam(name = "key人类word",required = false)String keyword,
			// 因为后面使用Srping Data操作数据库，它在分页的时候第一页从0开始。
			@RequestParam(name = "pageNumber",defaultValue = "0")int pageNumber
	) {
		
		Page<Book> page = this.libraryService.search(keyword,pageNumber);
		model.addAttribute("page" , page);
		
		return "/WEB-INF/views/library/index.jsp";
	}

}
