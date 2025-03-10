package ru.isands.test.estore.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import ru.isands.test.estore.dao.entity.ElectroShop;
import ru.isands.test.estore.dao.repo.ElectroShopRepository;
import ru.isands.test.estore.dto.ElectroShopDTO;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ElectroShopService {

    private final ElectroShopRepository electroShopRepository;

    public ElectroShopService(ElectroShopRepository electroShopRepository) {
        this.electroShopRepository = electroShopRepository;
    }

    public ElectroShopDTO add(ElectroShopDTO electroShopDTO) {
        ElectroShop electroShop = mapToEntity(electroShopDTO);
        ElectroShop savedElectroShop = electroShopRepository.save(electroShop);
        return mapToDTO(savedElectroShop);
    }

    public List<ElectroShopDTO> getAll(Integer start, Integer limit, Long shopId) {
        Stream<ElectroShop> electroShopStream =
                electroShopRepository.findAll().stream()
                .sorted(Comparator.comparing(ElectroShop::getShopId).thenComparing(ElectroShop::getElectroId));

        if (shopId != null) {
            electroShopStream =
                    electroShopStream.filter(i -> i.getShopId() == shopId);
        }

        electroShopStream = electroShopStream
                .skip(start)
                .limit(limit);

        return electroShopStream.map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public ElectroShopDTO getByElectroIdAndShopId(Long electroId, Long shopId) {
        return Optional.ofNullable(electroShopRepository.findByElectroIdAndShopId(electroId, shopId))
                .filter(list -> !list.isEmpty())
                .map(list -> list.get(0))
                .map(this::mapToDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Магазин-товар не найден"));
    }


    public ElectroShopDTO update(Long electroId, Long shopId, ElectroShopDTO electroShopDTO) {
        ElectroShop existingElectroShop = mapToEntity(getByElectroIdAndShopId(electroId, shopId));

        updateEntityFromDTO(electroShopDTO, existingElectroShop);
        ElectroShop updatedElectroShop = electroShopRepository.save(existingElectroShop);
        return mapToDTO(updatedElectroShop);
    }

    public void delete(Long electroId, Long shopId) {
        mapToEntity(getByElectroIdAndShopId(electroId, shopId));

        electroShopRepository.deleteByElectroIdAndShopId(electroId, shopId);
    }

    private ElectroShop mapToEntity(ElectroShopDTO electroShopDTO) {
        ElectroShop electroShop = new ElectroShop();
        updateEntityFromDTO(electroShopDTO, electroShop);
        return electroShop;
    }

    private void updateEntityFromDTO(ElectroShopDTO electroShopDTO, ElectroShop electroShop) {
        electroShop.setElectroId(electroShopDTO.getElectroId());
        electroShop.setShopId(electroShopDTO.getShopId());
        electroShop.setQuantity(electroShopDTO.getQuantity());
    }

    public void processCSVFile(MultipartFile file) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), Charset.forName("Windows-1251")))) {
            String line;
            List<ElectroShopDTO> electroShops = new ArrayList<>();

            boolean isFirstLine = true;
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                String[] data = line.split(";");

                if (data.length < 3) {
                    throw new RuntimeException("Файл содержит меньше столбцов, чем нужно");
                }

                ElectroShopDTO electroShopDTO = new ElectroShopDTO();
                electroShopDTO.setShopId(Long.parseLong(data[0].trim()));
                electroShopDTO.setElectroId(Long.parseLong(data[1].trim()));
                electroShopDTO.setQuantity(Integer.parseInt(data[2].trim()));

                electroShops.add(electroShopDTO);
            }

            saveAll(electroShops);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при обработке файла", e);
        }
    }

    public void saveAll(List<ElectroShopDTO> electroShopDTOs) {
        List<ElectroShop> electroShops = electroShopDTOs.stream()
                .map(this::mapToEntity)
                .collect(Collectors.toList());
        electroShops.forEach(i -> electroShopRepository.save(i));
    }

    private ElectroShopDTO mapToDTO(ElectroShop electroShop) {
        ElectroShopDTO electroShopDTO = new ElectroShopDTO();

        electroShopDTO.setElectroId(electroShop.getElectroId());
        electroShopDTO.setShopId(electroShop.getShopId());
        electroShopDTO.setQuantity(electroShop.getQuantity());

        return electroShopDTO;
    }
}