import {
    Box,
    Button,
    Grid,
    IconButton,
    Pagination,
    TextField
} from "@mui/material";
import React, {useEffect, useState} from "react";
import CommunityPost from "./CommunityPost";
import {searchPosts} from "../api/CommunityApi";
import SearchIcon from '@mui/icons-material/Search';

export const category = [
    {
        value: null,
        name: '전체',
    },
    {
        value: "자랑",
        name: '자랑',
    },
    {
        value: "산책",
        name: '산책',
    },
    {
        value: "Q&A",
        name: 'Q&A',
    },
];

export const searchOptions = [
    {
        value: null,
        name: "전체",
    },
    {
        value: "title",
        name: "제목",
    },
    {
        value: "content",
        name: "내용",
    },
];

function CommunityPostArea({handleCreateClick, handlePostClick}) {
    const [selectedCategory, setSelectedCategory] = useState(null);
    const [selectedSearchOption, setSelectedSearchOption] = useState("전체");
    const [currentPage, setCurrentPage] = useState(1);
    const [totalPage, setTotalPage] = useState(1);
    const [postList, setPostList] = useState([]);
    const [searchParams, setSearchParams] = useState({});
    const [keyword, setKeyword] = useState("");

    const handleCategoryClick = (category) => {
        setSelectedCategory(category);
        setCurrentPage(1);
        setKeyword("");
        setSearchParams({
            categoryName: category,
            page: 1,
        });
    };

    const handleSearchOptionChange = (event) => {
        setSelectedSearchOption(event.target.value);
    };

    const handlePageChange = (event, value) => {
        if (currentPage !== value) {
            setCurrentPage(value);
            setSearchParams((prevParams) => ({
                ...prevParams,
                page: value,
            }));
        }
    };

    const handleKeywordChange = (value) => {
        setKeyword(value);
    }

    function onClickSearch() {
        setSearchParams((prevParams) => ({
            ...prevParams,
            // column: selectedSearchOption,
            keyword: keyword,
        }));
    }

    useEffect(() => {
        searchPosts(searchParams)
        .then((response) => {
            const data = response.data;
            setPostList(data.postList);
            setTotalPage(data.pagingUtil.totalPages);
        })
        .catch((error) => {
            // 에러 처리
            console.error(error);
        });
    }, [searchParams]);

    return (
        <div>
            <Box sx={{
                display: 'flex',
                justifyContent: 'center',
                alignItems: 'center',
                width: '100%',
                p: 2,
            }}>
                {category.map(({value, name}) => (
                    <Button
                        key={value}
                        variant={selectedCategory === value ? 'contained'
                            : 'outlined'}
                        color="primary"
                        sx={{m: 1}}
                        onClick={() => handleCategoryClick(value)}
                    >
                        {name}
                    </Button>
                ))}
            </Box>
            <Box sx={{
                m: 2,
                display: 'flex',
                justifyContent: 'center',
                alignItems: 'center',
                flexGrow: 1
            }}>
                {/*<FormControl variant="standard"*/}
                {/*             sx={{mr: 'auto', minWidth: 120}}>*/}
                {/*    <Select*/}
                {/*        value={selectedSearchOption}*/}
                {/*        onChange={handleSearchOptionChange}*/}
                {/*        displayEmpty*/}
                {/*    >*/}
                {/*        {searchOptions.map(({value, name}) => (*/}
                {/*            <MenuItem key={value} value={value}>*/}
                {/*                {name}*/}
                {/*            </MenuItem>*/}
                {/*        ))}*/}
                {/*    </Select>*/}
                {/*</FormControl>*/}
                <form
                    onSubmit={(e) => {
                        e.preventDefault();
                        onClickSearch();
                    }}
                >
                    <TextField
                        label="키워드 검색"
                        variant="standard"
                        sx={{flexGrow: 1, ml: 2}}
                        value={keyword}
                        onChange={(e) => {
                            handleKeywordChange(e.target.value)
                        }}
                    />
                    <IconButton
                        type="submit"
                        aria-label="search"
                        sx={{width: '50px', height: '50px', mr: 2}}
                    >
                        <SearchIcon/>
                    </IconButton>
                </form>
                <Button variant="contained" sx={{ml: 'auto'}}
                        onClick={handleCreateClick}>
                    글 쓰기
                </Button>
            </Box>
            <Grid container spacing={2}>
                {postList && postList.map((post) => {
                    return (
                        <CommunityPost
                            key={post.id}
                            post={post}
                            handlePostClick={handlePostClick}
                        />
                    )
                })}
            </Grid>
            <Box sx={{display: 'flex', justifyContent: 'center', mt: 4}}>
                <Pagination count={totalPage} page={currentPage}
                            onChange={handlePageChange}/>
            </Box>
        </div>
    );
}

export default CommunityPostArea;