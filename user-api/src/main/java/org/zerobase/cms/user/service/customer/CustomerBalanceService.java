package org.zerobase.cms.user.service.customer;

import org.zerobase.cms.user.domain.customer.ChangeBalanceForm;
import org.zerobase.cms.user.domain.model.CustomerBalanceHistory;
import org.zerobase.cms.user.domain.repository.CustomerBalanceHistoryRepository;
import org.zerobase.cms.user.domain.repository.CustomerRepository;
import org.zerobase.cms.user.exception.CustomException;
import org.zerobase.cms.user.exception.ErrorCode;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomerBalanceService {

	private final CustomerBalanceHistoryRepository customerBalanceHistoryRepository;
	private final CustomerRepository customerRepository;

	// 해당 Exception이 나와있을 때 noRollbackFor 을 한단 말임
	@Transactional(noRollbackFor = {CustomException.class})
	public CustomerBalanceHistory changeBalance(Long customerId, ChangeBalanceForm form) throws CustomException {
		CustomerBalanceHistory customerBalanceHistory = customerBalanceHistoryRepository.findFirstByCustomer_IdOrderByIdDesc(customerId)
			.orElse(CustomerBalanceHistory.builder()
				.changeMoney(0)
				.currentMoney(0)
				.customer(customerRepository.findById(customerId)
					.orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER)))
				.build());
		if (customerBalanceHistory.getCurrentMoney() + form.getMoney() < 0) {
			throw new CustomException(ErrorCode.NOT_ENOUGH_BALANCE);
		}
		customerBalanceHistory = CustomerBalanceHistory.builder()
			.changeMoney(form.getMoney())
			.currentMoney(customerBalanceHistory.getCurrentMoney() + form.getMoney())
			.description(form.getMessage())
			.fromMessage(form.getFrom())
			.customer(customerBalanceHistory.getCustomer())
			.build();
		// 고객에 이중으로 잔액을 만들어 놓은 이유
		// 잔액 조회시 설정 안하면 CustomerBalanceHistory 테이블을 항상 조인해서 가져와야함 -> 안 그런 경우도 존재함
		// 뭔가를 계속 조인해서 가져오는 것은 좋지 않음
		customerBalanceHistory.getCustomer().setBalance(customerBalanceHistory.getCurrentMoney());
		return customerBalanceHistoryRepository.save(customerBalanceHistory);
	}
}
