package com.dailycodework.dreamshops.repository.voucher;

import com.dailycodework.dreamshops.entity.Voucher;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IVoucherRepository extends CrudRepository<Voucher, Long>, VoucherRepositoryCustom {
    Optional<Voucher> findByCodeAndCompanyId(String code, Long companyId);
}
