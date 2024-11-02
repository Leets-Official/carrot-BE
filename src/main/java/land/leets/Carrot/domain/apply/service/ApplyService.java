package land.leets.Carrot.domain.apply.service;

import static land.leets.Carrot.domain.apply.exception.ApplyErrorMessage.APPLY_NOT_FOUND;
import static land.leets.Carrot.domain.apply.exception.ApplyErrorMessage.POST_NOT_FOUND;
import static land.leets.Carrot.domain.apply.exception.ApplyErrorMessage.POST_NOT_RECRUITING;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import land.leets.Carrot.domain.apply.controller.SuccessMessage;
import land.leets.Carrot.domain.apply.domain.Applicant;
import land.leets.Carrot.domain.apply.dto.request.ApplyRequest;
import land.leets.Carrot.domain.apply.dto.response.GetApplicantResponse;
import land.leets.Carrot.domain.apply.entity.Apply;
import land.leets.Carrot.domain.apply.exception.ApplyException;
import land.leets.Carrot.domain.apply.repository.ApplyRepository;
import land.leets.Carrot.domain.post.repository.PostRepository;
import land.leets.Carrot.domain.user.entity.Employee;
import land.leets.Carrot.domain.user.repository.EmployeeRepository;
import land.leets.Carrot.global.common.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class ApplyService {
    private final ApplyRepository applyRepository;
    private final PostRepository postRepository;
    private final EmployeeRepository employeeRepository;

    private static final String POST_STATUS_RECRUITING = "recruiting";

    public void postApply(ApplyRequest applyRequest) {
        Apply apply = new Apply(postRepository.findById(applyRequest.postId())
                .orElseThrow(() -> new ApplyException(POST_NOT_FOUND)), applyRequest.userId());
        if (postRepository.findById(applyRequest.postId()).equals(POST_STATUS_RECRUITING)) {
            applyRepository.save(apply);
        } else {
            throw new ApplyException(POST_NOT_RECRUITING);
        }
    }

    public void setStatusRecruited(ApplyRequest applyRequest) {
        Apply apply = applyRepository.findByPostIdAndUserID(applyRequest.postId(), applyRequest.userId())
                .orElseThrow(() -> new ApplyException(APPLY_NOT_FOUND));
        apply.setIsRecruited(true);
    }

    public ResponseDto<GetApplicantResponse> getApplicant(Long postId) {
        List<Apply> applyList = applyRepository.findByPostId(postId);
        List<Employee> employeeList = employeeRepository.findByIdIn(applyList.stream()
                .map(Apply::getUserId)
                .collect(Collectors.toList()));
        List<Applicant> applicantList = employeeList.stream()
                .map(employee -> new Applicant(employee.getId(), employee.getEmployeeName(),
                        employee.getEmployeeAddress()))
                .collect(Collectors.toList());
        return new ResponseDto(SuccessMessage.GET_APPLICANT_LIST_SUCCESS.getCode(),
                SuccessMessage.GET_APPLICANT_LIST_SUCCESS.getMessage(), new GetApplicantResponse(applicantList));
    }
}
