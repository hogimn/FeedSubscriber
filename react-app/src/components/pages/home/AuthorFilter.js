import React, {useState} from 'react';

function AuthorFilter({authors, selectedAuthors, setSelectedAuthors}) {
    const [isOpen, setIsOpen] = useState(false);

    const toggleDropdown = () => {
        setIsOpen(!isOpen);
    };

    const handleAuthorSelect = (event) => {
        const selectedOptions = Array.from(event.target.selectedOptions, (option) => option.value);
        setSelectedAuthors(selectedOptions);
    };

    return (
        <div>
            <h3 onClick={toggleDropdown} style={{cursor: 'pointer'}}>
                Filter by Author
                {isOpen ? ' ▲' : ' ▼'}
            </h3>
            {isOpen && (
                <select multiple value={selectedAuthors} onChange={handleAuthorSelect}>
                    {authors.map((author, index) => (
                        <option key={index} value={author}>
                            {author}
                        </option>
                    ))}
                </select>
            )}
        </div>
    );
}

export default AuthorFilter;
