package poslovne.aplikacije.servisi;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import poslovne.aplikacije.RabbitMQConfigurator;
import poslovne.aplikacije.exceptions.ProizvodIsNullException;
import poslovne.aplikacije.exceptions.ProizvodNotFoundException;
import poslovne.aplikacije.messaging.ProizvodEvent;
import poslovne.aplikacije.proizvodi.Proizvod;
import poslovne.aplikacije.repository.ProizvodRepository;

@Service
public class ProizvodService {
    @Autowired
    private ProizvodRepository proizvodRepository;
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    public Proizvod noviProizvod(@RequestBody Proizvod noviProizvod) {
        if (noviProizvod == null) throw new ProizvodIsNullException();
        proizvodRepository.save(noviProizvod);
        rabbitTemplate.convertAndSend(RabbitMQConfigurator.PROIZVODI_TOPIC_EXCHANGE_NAME, 
            "proizvodi.events.create",
            ProizvodEvent.createNewProizvodEvent(noviProizvod));
        return noviProizvod;
    }
    
    public Proizvod updateProizvod(Proizvod noviProizvod, Long id) {
        Proizvod updatedProizvod = proizvodRepository.findById(id)
            .orElseThrow(() -> new ProizvodIsNullException());
        updatedProizvod.updateProizvod(noviProizvod);
        proizvodRepository.save(updatedProizvod);
        rabbitTemplate.convertAndSend(RabbitMQConfigurator.PROIZVODI_TOPIC_EXCHANGE_NAME, 
            "proizvodi.events.update",
            ProizvodEvent.createUpdatedProizvodEvent(updatedProizvod));
        return updatedProizvod;
    }
    
    public Proizvod deleteProizvod(Long id) {
        Proizvod p = proizvodRepository.findById(id)
            .orElseThrow(() -> new ProizvodNotFoundException(id));
        proizvodRepository.deleteById(id);
        rabbitTemplate.convertAndSend(RabbitMQConfigurator.PROIZVODI_TOPIC_EXCHANGE_NAME, 
            "proizvodi.events.delete",
            ProizvodEvent.createDeletedProizvodEvent(p));
        return p;
    }
}