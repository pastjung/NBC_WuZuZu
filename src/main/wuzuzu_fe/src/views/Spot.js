// YourComponent.js

import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Box, Button, List, ListItem, ListItemText, MenuItem, Select } from '@mui/material';

const YourComponent = () => {
  const [selectedOption, setSelectedOption] = useState('');
  const [items, setItems] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);
  const navigate = useNavigate();

  const handleOptionChange = (event) => {
    setSelectedOption(event.target.value);
  };

  const handlePageClick = (pageNumber) => {
    setCurrentPage(pageNumber);
    fetchData(selectedOption, pageNumber);
  };

  const fetchData = async (selectedOption, pageNumber) => {
    try {
      const response = await fetch(`/api/v1/spots/${selectedOption}/${pageNumber}`, {
        headers: {
          'Authorization': localStorage.getItem('Authorization'),
        },
      });

      if (response.ok) {
        const responseData = await response.json();
        if (responseData.hasOwnProperty('data') && Array.isArray(responseData.data)) {
          setItems(responseData.data);
        } else {
          console.error('Data is not in the expected format');
        }
      } else {
        console.error('Failed to fetch data');
      }
    } catch (error) {
      console.error('Error fetching data:', error);
    }
  };

  useEffect(() => {
    if (selectedOption) {
      fetchData(selectedOption, currentPage);
    }
  }, [selectedOption, currentPage]);

  const handleGoBack = () => {
    navigate(-1);
  };

  const handleItemClick = (spotName, address, selectedOption) => {
    // 클릭한 항목의 spotName과 address를 상세 페이지로 전달하고, 상세 페이지로 이동
    navigate('/SpotDetail', { state: { spotName, address ,selectedOption} });
  };

  return (
      <Box>
        <Button onClick={handleGoBack}>이전 페이지로 돌아가기</Button>
        <Box display="flex" justifyContent="flex-end" alignItems="center" height="100vh">
          <Box style={{ position: 'absolute', top: '10%', left: '80%', transform: 'translateY(-50%)', marginRight: '20px' }}>
            <Select
                value={selectedOption}
                onChange={handleOptionChange}
                style={{ width: '200px' }}
            >
              <MenuItem value="동물병원">동물병원</MenuItem>
              <MenuItem value="애견카페">애견카페</MenuItem>
              <MenuItem value="반려동물 미용">반려동물 미용</MenuItem>
            </Select>
          </Box>
          <Box style={{ position: 'absolute', top: '50%', left: '50%', transform: 'translate(-50%, -50%)', textAlign: 'center', width: '1500px' }}>
            {items.length > 0 ? (
                <List style={{ border: '1px solid #ccc', borderRadius: '4px' }}>
                  {items.map((item, index) => (
                      <ListItem key={index} button onClick={() => handleItemClick(item.spotName, item.address,selectedOption )}>
                        <ListItemText primary={item.spotName} secondary={item.address} />
                      </ListItem>
                  ))}
                </List>
            ) : (
                <div style={{ color: '#888' }}>검색 결과가 없습니다</div>
            )}
            <Pagination totalPages={5} currentPage={currentPage} onPageClick={handlePageClick} />
          </Box>
        </Box>
      </Box>
  );
};

const Pagination = ({ totalPages, currentPage, onPageClick }) => {
  return (
      <div style={{ marginTop: '20px' }}>
        {Array.from({ length: totalPages }, (_, index) => index + 1).map(pageNumber => (
            <button key={pageNumber} onClick={() => onPageClick(pageNumber)} style={{ color: currentPage === pageNumber ? 'blue' : 'black' }}>{pageNumber}</button>
        ))}
      </div>
  );
};

export default YourComponent;
