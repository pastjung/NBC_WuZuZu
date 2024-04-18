// DetailPage.js

import React, { useEffect, useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { Button } from '@mui/material';
import sampleImage from '../assets/img/spotDetail.jpg';

const DetailPage = () => {
  const [detail, setDetail] = useState({});
  const location = useLocation();
  const navigate = useNavigate();
  const { spotName, address, selectedOption } = location.state;
  const category = selectedOption;

  const handleGoBack = () => {
    navigate(-1);
  };

  const handleBookmark = async () => {
    try {
      // Call your API here to perform the login
      const response = await fetch('api/v1/favorites', {
        method: 'POST',
        headers: {
          'Authorization': localStorage.getItem('Authorization'),
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(
            {spotName, address, category}),
      });

      if (response.ok) {
        alert("즐겨찾기에 넣어뒀습니다")
        navigate("/Spot");
        // Handle successful login (e.g., set user state, redirect, etc.)
      } else {
        console.error('no');
      }
    } catch (error) {
      console.error('Error logging in:', error);

    }
  };

  useEffect(() => {
    const fetchData = async () => {
      try {
        const requestOptions = {
          method: 'GET',
          headers: {
            'Authorization': localStorage.getItem('Authorization'),
          },
        };
        const response = await fetch(`/api/v1/spots/detail/${spotName}`, requestOptions);
        const result = await response.json();
        setDetail(result);
      } catch (error) {
        console.error('Error fetching detail:', error);
      }
    };

    fetchData();
  }, [spotName]);

  return (
      <div style={{ display: 'flex',justifyContent: 'center', alignItems: 'center', height: '100vh' }}>
        <div style={{
          backgroundColor: 'white',
          border: '1px solid #ccc',
          borderRadius: '10px',
          padding: '20px',
          marginBottom: '400px',
          width: '40%',
        }}>
          <div style={{
            backgroundColor: '#FBEFEF',
            padding: '10px',
            borderRadius: '10px',
            position: 'relative', // 이미지와 버튼을 포함한 컨테이너에 상대적인 위치를 지정합니다.
            textAlign: 'center'
          }}>
            <h1 style={{ textAlign: 'center' }}>{spotName}</h1>
            <h2>Address: {address}</h2>
            {detail && detail.data && (
                <>
                  <h2>Category: {detail.data.category}</h2>
                  <h2>Telephone: {detail.data.telephone}</h2>
                  <h2>Link: {detail.data.link}</h2>
                </>
            )}
            {/* 추가적인 상세 정보를 표시하거나 필요한 구성요소를 여기에 추가할 수 있습니다. */}
          </div>
          {/* 이미지 */}
          <img
              src={sampleImage}
              alt="Sample"
              style={{ width: '650px', height: 'auto', position: 'absolute', bottom: '280px', left: '60px', zIndex: '-1' }}
          />
          <div style={{
            marginTop: '20px',
            position: 'absolute',
            top: '50%',
            left: '50%',
            transform: 'translate(-50%, -50%)',
            textAlign: 'center',
            width: '1500px'
          }}>
            <Button
                variant="contained"
                onClick={handleGoBack}
                style={{ backgroundColor: '#FBEFEF', color: '#6E6E6E', marginRight: '10px', width: '150px', height: '50px' }}
            >
              돌아가기
            </Button>
            <Button
                variant="contained"
                onClick={handleBookmark}
                style={{ backgroundColor: '#FBEFEF', color: '#6E6E6E', width: '150px', height: '50px' }}
            >
              즐겨찾기 추가
            </Button>
          </div>
        </div>
      </div>
  );
};

export default DetailPage;
