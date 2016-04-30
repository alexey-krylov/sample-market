package market.web;

import market.domain.Retailer;
import market.repository.RetailerRepository;
import market.web.dto.RetailerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/retailers")
public class RetailerResource {

    /**
     * Sample CRUD controller.
     */

    @Autowired
    private RetailerRepository retailerRepository;

    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Retailer>> getAllRetailers(Pageable pageable) {
        Page<Retailer> page = retailerRepository.findAll(pageable);
        return new ResponseEntity<>(page.getContent(), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Retailer> getById(@PathVariable Long id) {
        return retailerRepository.findOneById(id)
                .map(retailer -> new ResponseEntity<>(retailer, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateById(@PathVariable Long id, @Valid @RequestBody RetailerDTO retailerDTO) {
        return retailerRepository.findOneById(id)
                .map(retailer -> {
                    retailer.setName(retailerDTO.getName());
                    retailerRepository.save(retailer);
                    return new ResponseEntity<>(HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        return retailerRepository.findOneById(id)
                .map(retailer -> {
                    retailerRepository.delete(id);
                    return new ResponseEntity<>(HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
