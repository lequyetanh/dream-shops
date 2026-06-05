package com.dailycodework.dreamshops.service.category;

import com.dailycodework.dreamshops.constant.ResultNotify;
import com.dailycodework.dreamshops.entity.Category;
import com.dailycodework.dreamshops.payload.dto.BaseResultDTO;
import com.dailycodework.dreamshops.payload.dto.category.CategoryInfo;
import com.dailycodework.dreamshops.repository.category.ICategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService implements ICategoryService {
    private final ICategoryRepository categoryRepository;

    @Override
    @Transactional(readOnly = true)
    public BaseResultDTO getCategoryWithPaging(Pageable pageable, Long companyId, String keyword) {
        Page<Category> page = categoryRepository.findWithPaging(companyId, keyword, pageable);
        return new BaseResultDTO(ResultNotify.successGet, true, page.getContent(), (int) page.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public BaseResultDTO findById(Long id) {
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isEmpty()) {
            throw new RuntimeException(ResultNotify.notFound);
        }
        return new BaseResultDTO(ResultNotify.successGet, true, category.get());
    }

    @Override
    public BaseResultDTO createCategory(CategoryInfo categoryReq) {
        Category category = new Category();
        category.setName(categoryReq.getName());
        category.setDescription(categoryReq.getDescription());
        category.setCompanyId(categoryReq.getCompanyId());
        categoryRepository.save(category);
        return new BaseResultDTO(ResultNotify.successCreate, true, null);
    }

    @Override
    public BaseResultDTO updateCategory(CategoryInfo categoryReq) {
        Optional<Category> existing = categoryRepository.findById(categoryReq.getId());
        if (existing.isEmpty()) {
            throw new RuntimeException(ResultNotify.notFound);
        }
        Category category = existing.get();
        category.setName(categoryReq.getName());
        category.setDescription(categoryReq.getDescription());
        categoryRepository.save(category);
        return new BaseResultDTO(ResultNotify.successUpdate, true, category);
    }

    @Override
    public BaseResultDTO deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new RuntimeException(ResultNotify.notFound);
        }
        categoryRepository.deleteById(id);
        return new BaseResultDTO(ResultNotify.successDelete, true, null);
    }
}
