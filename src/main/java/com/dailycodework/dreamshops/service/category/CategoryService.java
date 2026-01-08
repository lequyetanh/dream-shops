package com.dailycodework.dreamshops.service.category;

import com.dailycodework.dreamshops.constant.ResultNotify;
import com.dailycodework.dreamshops.dto.BaseResultDTO;
import com.dailycodework.dreamshops.dto.category.CategroyInfo;
import com.dailycodework.dreamshops.entity.Category;
import com.dailycodework.dreamshops.mapper.category.CategoryMapper;
import com.dailycodework.dreamshops.repository.category.ICategoryRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService implements ICategoryService {
    private final ICategoryRepository categoryRepository;
//    private final CategoryMapper categoryMapper;

    public CategoryService(
            ICategoryRepository categoryRepository
//            CategoryMapper categoryMapper
    ) {
        this.categoryRepository = categoryRepository;
//        this.categoryMapper = categoryMapper;
    }

    @Override
    public BaseResultDTO getCategoryWithPaging(
            Pageable pageable,
            String keyword
    ) {
        List<Category> listCategory = categoryRepository.findAll();
        return new BaseResultDTO(
                ResultNotify.successCreate,
                true,
                listCategory
        );
    }

    @Override
    public BaseResultDTO findById(Long id){
        String categoryName = categoryRepository.getNameById(id);
        if(categoryName == null){
            throw new RuntimeException("Không tìm thấy nhóm sản phẩm");
        }
        return new BaseResultDTO(
                ResultNotify.successGet,
                true,
                categoryName
        );
    }

    @Override
    public BaseResultDTO createCategory(CategroyInfo categoryReq) {
//        Category category = categoryMapper.toCategory(categoryReq);
        Category category = new Category();
        category.setName(categoryReq.getName());
        category.setDescription(categoryReq.getDescription());
        categoryRepository.save(category);
        return new BaseResultDTO(
                ResultNotify.successCreate,
                true,
                null
        );
    }

    @Override
    public BaseResultDTO updateCategory(CategroyInfo categoryReq) {
        return null;
    }

    @Override
    public BaseResultDTO deleteCategory(Long id) {
        return null;
    }
}
