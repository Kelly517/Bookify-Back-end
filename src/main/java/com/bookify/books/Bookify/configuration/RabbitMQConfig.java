package com.bookify.books.Bookify.configuration;

// Importaciones necesarias para configurar RabbitMQ
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Nombre de la cola que usará el sistema para procesar imágenes de portada
    public static final String COVER_IMAGE_QUEUE = "cover-image-queue";

    // Define una cola persistente con el nombre "cover-image-queue"
    @Bean
    public Queue coverImageQueue() {
        return new Queue(COVER_IMAGE_QUEUE, true); // 'true' indica que la cola es duradera (sobrevive reinicios del servidor)
    }

    // Convierte los mensajes de RabbitMQ a formato JSON y viceversa usando Jackson
    @Bean
    public MessageConverter jsonMessageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();

        // Se especifica que solo se pueden confiar en las clases del paquete indicado para evitar problemas de seguridad
        DefaultClassMapper classMapper = new DefaultClassMapper();
        classMapper.setTrustedPackages("com.bookify.books.Bookify.model");

        // Se aplica ese classMapper al convertidor JSON
        converter.setClassMapper(classMapper);
        return converter;
    }

    // Configura el template de RabbitMQ que se usará para enviar mensajes
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);

        // Establece el convertidor de mensajes como JSON
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }

    // Configura el contenedor que escucha mensajes desde RabbitMQ
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();

        // Se indica qué conexión usar para escuchar mensajes
        factory.setConnectionFactory(connectionFactory);

        // También se indica que se usará JSON para convertir mensajes entrantes
        factory.setMessageConverter(jsonMessageConverter());
        return factory;
    }
}
