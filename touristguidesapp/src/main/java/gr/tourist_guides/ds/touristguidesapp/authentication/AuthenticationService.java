package gr.tourist_guides.ds.touristguidesapp.authentication;

import gr.tourist_guides.ds.touristguidesapp.core.exceptions.AppObjectNotAuthorizedException;
import gr.tourist_guides.ds.touristguidesapp.dto.AuthenticationRequestDTO;
import gr.tourist_guides.ds.touristguidesapp.dto.AuthenticationResponseDTO;
import gr.tourist_guides.ds.touristguidesapp.model.Guide;
import gr.tourist_guides.ds.touristguidesapp.model.User;
import gr.tourist_guides.ds.touristguidesapp.model.Visitor;
import gr.tourist_guides.ds.touristguidesapp.repository.GuideRepository;
import gr.tourist_guides.ds.touristguidesapp.repository.UserRepository;
import gr.tourist_guides.ds.touristguidesapp.repository.VisitorRepository;
import gr.tourist_guides.ds.touristguidesapp.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final GuideRepository guideRepository;
    private final VisitorRepository visitorRepository;

    public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO dto)
            throws AppObjectNotAuthorizedException {
        String uuid = "";

        // Create an authentication token from username and password
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.username(), dto.password()));

        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(() ->
                new AppObjectNotAuthorizedException("User", "User not authorized"));

        // If authentication was successful, generate JWT token
        // Get uuid from Guide or Visitor related to User
        if (user.isGuide()) {
            Guide guide = guideRepository.findByUserUsername(user.getUsername()).get();
            uuid = guide.getUuid();
        }
        if (user.isVisitor()) {
            Visitor visitor = visitorRepository.findByUserUsername(user.getUsername()).get();
            uuid = visitor.getUuid();
        }
        String token = jwtService.generateToken(authentication.getName(), uuid, user.getFirstname(), user.getLastname(), user.getRole().name());
        return new AuthenticationResponseDTO(token);
    }
}
