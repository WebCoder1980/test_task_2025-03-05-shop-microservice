package ru.isands.test.estore.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import ru.isands.test.estore.dao.entity.Shop;
import ru.isands.test.estore.dao.repo.ShopRepository;
import ru.isands.test.estore.dto.ShopDTO;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShopService {

    private final ShopRepository shopRepository;

    public ShopService(ShopRepository shopRepository) {
        this.shopRepository = shopRepository;
    }

    public ShopDTO add(ShopDTO shopDTO) {
        Shop shop = mapToEntity(shopDTO);
        Shop savedShop = shopRepository.save(shop);
        return mapToDTO(savedShop);
    }

    public List<ShopDTO> getAll(int start, int limit) {
        return shopRepository.findAll().stream()
                .sorted(Comparator.comparing(Shop::getId))
                .skip(start)
                .limit(limit)
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public ShopDTO getById(Long id) {
        return shopRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Магазин не найден"));
    }

    public ShopDTO update(Long id, ShopDTO shopDTO) {
        Shop existingShop = shopRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Магазин не найден"));

        updateEntityFromDTO(shopDTO, existingShop);
        Shop updatedShop = shopRepository.save(existingShop);
        return mapToDTO(updatedShop);
    }

    public void delete(Long id) {
        if (!shopRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Магазин не найден");
        }
        shopRepository.deleteById(id);
    }

    private Shop mapToEntity(ShopDTO shopDTO) {
        Shop shop = new Shop();
        updateEntityFromDTO(shopDTO, shop);
        return shop;
    }

    private void updateEntityFromDTO(ShopDTO shopDTO, Shop shop) {
        shop.setName(shopDTO.getName());
        shop.setAddress(shopDTO.getAddress());
    }

    public void processCSVFile(MultipartFile file) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), Charset.forName("Windows-1251")))) {
            String line;
            List<ShopDTO> shops = new ArrayList<>();

            boolean isFirstLine = true;
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                String[] data = line.split(";");

                ShopDTO shopDTO = new ShopDTO();
                shopDTO.setName(data[1].trim());
                shopDTO.setAddress(data[2].trim());

                shops.add(shopDTO);
            }

            saveAll(shops);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при обработке файла", e);
        }
    }

    public void saveAll(List<ShopDTO> shopDTOs) {
        List<Shop> shops = shopDTOs.stream()
                .map(this::mapToEntity)
                .collect(Collectors.toList());
        shops.forEach(i -> shopRepository.save(i));
    }

    private ShopDTO mapToDTO(Shop shop) {
        ShopDTO shopDTO = new ShopDTO();
        shopDTO.setId(shop.getId());
        shopDTO.setName(shop.getName());
        shopDTO.setAddress(shop.getAddress());
        return shopDTO;
    }
}